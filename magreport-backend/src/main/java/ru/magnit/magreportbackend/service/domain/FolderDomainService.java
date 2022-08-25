package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.folderreport.Folder;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthority;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.folderreport.FolderReport;
import ru.magnit.magreportbackend.domain.folderreport.FolderRole;
import ru.magnit.magreportbackend.domain.folderreport.FolderRolePermission;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.domain.user.SystemRoles;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderTypes;
import ru.magnit.magreportbackend.dto.request.folderreport.FolderAddReportRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportIdRequest;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResultResponse;
import ru.magnit.magreportbackend.dto.response.folder.ReportSearchResultResponse;
import ru.magnit.magreportbackend.dto.response.folderreport.FolderResponse;
import ru.magnit.magreportbackend.dto.response.report.PublishedReportResponse;
import ru.magnit.magreportbackend.dto.response.role.RoleFolderResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.folder.FolderNodeResponseFolderMapper;
import ru.magnit.magreportbackend.mapper.folderreport.FolderMapper;
import ru.magnit.magreportbackend.mapper.folderreport.FolderReportMapper;
import ru.magnit.magreportbackend.mapper.folderreport.FolderResponseMapper;
import ru.magnit.magreportbackend.mapper.folderreport.FolderRolePermissionMapper;
import ru.magnit.magreportbackend.mapper.report.PublishedReportResponseMapper;
import ru.magnit.magreportbackend.mapper.report.ReportResponseMapper;
import ru.magnit.magreportbackend.repository.FavReportRepository;
import ru.magnit.magreportbackend.repository.FolderReportRepository;
import ru.magnit.magreportbackend.repository.FolderRepository;
import ru.magnit.magreportbackend.repository.FolderRoleRepository;
import ru.magnit.magreportbackend.repository.ReportRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderDomainService {

    private final FolderRepository repository;
    private final FolderReportRepository folderReportRepository;
    private final ReportRepository reportRepository;
    private final UserDomainService userDomainService;
    private final FavReportRepository favReportRepository;
    private final FolderRoleRepository folderRoleRepository;

    private final FolderPermissionsDomainService folderPermissionsDomainService;

    private final FolderMapper folderMapper;
    private final FolderReportMapper folderReportMapper;
    private final ReportResponseMapper reportResponseMapper;
    private final FolderResponseMapper folderResponseMapper;
    private final FolderRolePermissionMapper folderRolePermissionMapper;
    private final PublishedReportResponseMapper publishedReportResponseMapper;
    private final FolderNodeResponseFolderMapper folderNodeResponseFolderMapper;

    @Value("${magreport.max-level-hierarchy}")
    Long maxLevel;

    @Transactional
    public FolderResponse addFolder(FolderAddRequest request) {

        var folder = folderMapper.from(request);
        folder = repository.saveAndFlush(folder);

        if (repository.checkRingPath(folder.getId()).size() == maxLevel)
            throw new InvalidParametersException("Invalid parent id folder: path is looped");

        return folderResponseMapper.from(folder);
    }

    @Transactional
    public FolderResponse getFolder(Long userId, Long folderId) {

        if (folderId != null) checkFolderExists(folderId);

        FolderResponse result;

        if (folderId == null) {
            result = new FolderResponse()
                    .setName("root")
                    .setChildFolders(folderResponseMapper.shallowMap(repository.getAllByParentFolderIsNull()))
                    .setCreated(LocalDateTime.now())
                    .setModified(LocalDateTime.now());
        } else {
            var folder = repository.getReferenceById(folderId);

            result = folderResponseMapper.from(folder);
        }

        result.getReports().forEach(report -> report.setFavorite(favReportRepository.existsByUserIdAndReportId(userId, report.getId())));

        return result;
    }

    @Transactional
    public List<FolderResponse> getChildFolders(Long parentId) {
        var folders = repository.getAllByParentFolderId(parentId);
        return folderResponseMapper.from(folders);
    }

    @Transactional
    public List<Long> getReportsFolders(Long reportId) {
        final var folders = folderReportRepository.findByReportId(reportId);

        return folders.stream().map(folderReport -> folderReport.getFolder().getId()).toList();
    }

    @Transactional
    public FolderResponse renameFolder(FolderRenameRequest request) {

        checkFolderExists(request.getId());

        var folder = repository.getReferenceById(request.getId());

        folder
                .setName(request.getName())
                .setDescription(request.getDescription());

        folder = repository.save(folder);

        return folderResponseMapper.from(folder);
    }

    @Transactional
    public void deleteFolder(Long folderId) {

        checkFolderExists(folderId);
        checkFolderEmpty(folderId);

        repository.deleteById(folderId);
    }

    @Transactional
    public List<RoleFolderResponse> getFoldersPermittedToRole(Long roleId) {
        final var folderRoles = folderRoleRepository.getAllByRoleId(roleId);

        return folderRoles.stream().map(folderRole -> new RoleFolderResponse(
                folderRole.getFolder().getId(),
                folderRole.getFolder().getName(),
                folderRole.getPermissions().stream()
                        .map(FolderRolePermission::getAuthority)
                        .max(Comparator.comparing(FolderAuthority::getId))
                        .map(perm -> FolderAuthorityEnum.getById(perm.getId()))
                        .orElse(FolderAuthorityEnum.NONE),
                FolderTypes.PUBLISHED_REPORT
        )).toList();
    }

    @Transactional
    public void addFolderPermittedToRole(List<Long> folders, Long roleId, List<FolderAuthorityEnum> permissions) {

        var folderRoles = folders.stream().map(folder -> {
            var res = new FolderRole()
                    .setFolder(new Folder(folder))
                    .setRole(new Role(roleId))
                    .setPermissions(folderRolePermissionMapper.from(permissions));
            res.getPermissions().forEach(p -> p.setFolderRole(res));
           return res;
        }).toList();

        folderRoleRepository.saveAll(folderRoles);
    }

    @Transactional
    public void deleteFolderPermittedToRole(List<Long> folderIds, Long roleId) {
        folderRoleRepository.deleteAllByFolderIdInAndRoleId(folderIds,roleId);
    }

    @Transactional
    public void addReport(FolderAddReportRequest request) {

        checkReportExists(request.getReportId());
        checkFolderExists(request.getFolderId());
        checkReportAlreadyInFolder(request.getFolderId(), request.getReportId());

        var folderReport = folderReportMapper.from(request);
        folderReport.setUser(new User(userDomainService.getCurrentUser().getId()));

        folderReportRepository.save(folderReport);
    }

    @Transactional
    public void deleteReport(FolderAddReportRequest request) {

        folderReportRepository.deleteByFolderIdAndReportId(request.getFolderId(), request.getReportId());
    }

    @Transactional
    public FolderSearchResponse searchFolder(FolderSearchRequest request) {

        var result = new FolderSearchResponse(new LinkedList<>(), new LinkedList<>());

        if (request.getRootFolderId() == null) {
            final var folders = repository.getAllByParentFolderIsNull();
            folders.forEach(folder -> searchFolder(folder, request, result));

            return result;
        } else {

            final var folder = repository.getReferenceById(request.getRootFolderId());

            return searchFolder(folder, request, result);
        }
    }

    @Transactional
    public PublishedReportResponse getPublishedReports(ReportIdRequest request) {

        var folderReports = folderReportRepository.findByReportId(request.getId());
        if (folderReports.isEmpty()) return null;

        var response = publishedReportResponseMapper.from(folderReports.get(0).getReport());

        response.setFolders(folderReports.stream()
                .map(FolderReport::getFolder)
                .map(f -> new FolderSearchResultResponse(getPath(f), folderNodeResponseFolderMapper.from(f)))
                .collect(Collectors.toList()));

        return response;
    }

    @Transactional
    public FolderResponse changeParentFolder(FolderChangeParentRequest request) {
        final var folder = repository.getReferenceById(request.getId());
        final var parentFolder = request.getParentId() == null ? null : new Folder(request.getParentId());
        folder.setParentFolder(parentFolder);
        repository.save(folder);

        if (repository.checkRingPath(request.getId()).size() == maxLevel)
            throw new InvalidParametersException("Invalid new parent id folder: path is looped");

        return folderResponseMapper.from(folder);
    }

    @Transactional
    public List<FolderNodeResponse> getPathToFolder(@NonNull Long folderId) {
        checkFolderExists(folderId);

        return getPath(repository.getReferenceById(folderId));
    }

    private FolderSearchResponse searchFolder(Folder folder, FolderSearchRequest request, FolderSearchResponse result) {

        if (folder == null) return result;

        var permissions = folderPermissionsDomainService.getFolderReportPermissions(folder.getId()).rolePermissions();

        final var currentUser = userDomainService.getCurrentUser();
        final var userRoles = userDomainService.getUserRoles(currentUser.getName(), null).stream().map(RoleView::getId).collect(Collectors.toSet());

        if(!(userRoles.contains(SystemRoles.ADMIN.getId()) ||
                permissions.stream().anyMatch(rolePermission -> userRoles.contains(rolePermission.role().getId()))))
            return result;

        final var reports = folder.getFolderReports()
                .stream()
                .map(FolderReport::getReport)
                .filter(report -> request.getLikenessType().check(request.getSearchString(), report.getName()))
                .collect(Collectors.toList());

        reports.forEach(report -> result.objects().add(mapReport(report, getPath(folder))));

        if (request.getLikenessType().check(request.getSearchString(), folder.getName()) && folder.getParentFolder() == null)
            result.folders().add(mapFolder(folder, getPath(folder)));

        final var folders = folder.getChildFolders()
                .stream()
                .filter(childFolder -> request.getLikenessType().check(request.getSearchString(), childFolder.getName()))
                .collect(Collectors.toList());

        folders.forEach(childFolder ->{

            var childPermissions = folderPermissionsDomainService.getFolderReportPermissions(folder.getId()).rolePermissions();
            if(userRoles.contains(SystemRoles.ADMIN.getId()) ||
                    childPermissions.stream().anyMatch(rolePermission -> userRoles.contains(rolePermission.role().getId()))) {
                result.folders().add(mapFolder(childFolder, getPath(childFolder)));
            }

        });

        if (request.isRecursive())
            folder.getChildFolders().forEach(childFolder -> searchFolder(childFolder, request, result));

        return result;
    }

    private FolderSearchResultResponse mapFolder(Folder folder, List<FolderNodeResponse> path) {

        return new FolderSearchResultResponse(path, new FolderNodeResponse(folder.getId(), folder.getParentFolder() == null ? null : folder.getParentFolder().getId(), folder.getName(), folder.getDescription(),folder.getCreatedDateTime(),folder.getModifiedDateTime()));
    }

    private List<FolderNodeResponse> getPath(Folder folder) {
        var result = new LinkedList<FolderNodeResponse>();

        result.add(folderNodeResponseFolderMapper.from(folder));

        while (folder.getParentFolder() != null) {
            folder = folder.getParentFolder();
            result.addFirst(folderNodeResponseFolderMapper.from(folder));
        }

        return result;
    }

    private ReportSearchResultResponse mapReport(Report report, List<FolderNodeResponse> path) {
        return new ReportSearchResultResponse(path, reportResponseMapper.from(report));
    }

    private void checkReportAlreadyInFolder(Long folderId, Long reportId) {

        if (folderReportRepository.existsByFolderIdAndReportId(folderId, reportId))
            throw new InvalidParametersException("Report with id:" + reportId + " already exists in folder with id: " + folderId + ".");
    }

    private void checkFolderExists(Long id) {

        if (!isFolderExists(id))
            throw new InvalidParametersException("Folder with id:" + id + " does not exists.");
    }

    private boolean isFolderExists(Long id) {

        return repository.existsById(id);
    }

    private void checkFolderEmpty(Long id) {
        if (!isFolderEmpty(id))
            throw new InvalidParametersException("Folder with id: " + id + " is not empty.");
    }

    private boolean isFolderEmpty(Long id) {

        return !repository.existsByParentFolderId(id) &&
                !folderReportRepository.existsByFolderId(id);
    }

    private void checkReportExists(Long id) {

        if (!isReportExists(id))
            throw new InvalidParametersException("Folder with id:" + id + " does not exists.");
    }

    private boolean isReportExists(Long id) {

        return reportRepository.existsById(id);
    }
}
