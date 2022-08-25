package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.BaseEntity;
import ru.magnit.magreportbackend.domain.favorite.FavReport;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportGroup;
import ru.magnit.magreportbackend.domain.folderreport.Folder;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthority;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.report.ReportFolder;
import ru.magnit.magreportbackend.domain.report.ReportFolderRole;
import ru.magnit.magreportbackend.domain.report.ReportFolderRolePermission;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobStatusEnum;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.request.ChangeParentFolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.CopyFolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderTypes;
import ru.magnit.magreportbackend.dto.request.report.ReportAddFavoritesRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportAddRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportEditRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportIdRequest;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetFieldResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.dto.response.folderreport.FolderResponse;
import ru.magnit.magreportbackend.dto.response.report.PivotFieldTypeResponse;
import ru.magnit.magreportbackend.dto.response.report.PublishedReportResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportFolderResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportShortResponse;
import ru.magnit.magreportbackend.dto.response.role.RoleFolderResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.folder.FolderNodeResponseFolderMapper;
import ru.magnit.magreportbackend.mapper.report.FolderNodeResponseReportFolderMapper;
import ru.magnit.magreportbackend.mapper.report.PivotFieldTypeResponseMapper;
import ru.magnit.magreportbackend.mapper.report.PublishedReportResponseMapper;
import ru.magnit.magreportbackend.mapper.report.ReportCloner;
import ru.magnit.magreportbackend.mapper.report.ReportFieldMapperDataSet;
import ru.magnit.magreportbackend.mapper.report.ReportFolderCloner;
import ru.magnit.magreportbackend.mapper.report.ReportFolderMapper;
import ru.magnit.magreportbackend.mapper.report.ReportFolderResponseMapper;
import ru.magnit.magreportbackend.mapper.report.ReportFolderRoleCloner;
import ru.magnit.magreportbackend.mapper.report.ReportFolderRolePermissionMapper;
import ru.magnit.magreportbackend.mapper.report.ReportMapper;
import ru.magnit.magreportbackend.mapper.report.ReportMerger;
import ru.magnit.magreportbackend.mapper.report.ReportResponseMapper;
import ru.magnit.magreportbackend.mapper.report.ReportShortResponseMapper;
import ru.magnit.magreportbackend.repository.FavReportRepository;
import ru.magnit.magreportbackend.repository.PivotFieldTypeRepository;
import ru.magnit.magreportbackend.repository.ReportFieldRepository;
import ru.magnit.magreportbackend.repository.ReportFolderRepository;
import ru.magnit.magreportbackend.repository.ReportFolderRoleRepository;
import ru.magnit.magreportbackend.repository.ReportRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class ReportDomainService {

    private final ReportFolderRepository folderRepository;
    private final ReportRepository reportRepository;
    private final ReportFieldRepository reportFieldRepository;
    private final PivotFieldTypeRepository pivotFieldTypeRepository;
    private final DataSetDomainService dataSetDomainService;
    private final FavReportRepository favReportRepository;
    private final ReportFolderRoleRepository reportFolderRoleRepository;

    private final ReportMapper reportMapper;
    private final ReportMerger reportMerger;
    private final ReportFolderMapper reportFolderMapper;
    private final ReportResponseMapper reportResponseMapper;
    private final ReportFieldMapperDataSet reportFieldMapperDataSet;
    private final ReportShortResponseMapper reportShortResponseMapper;
    private final ReportFolderResponseMapper reportFolderResponseMapper;
    private final PivotFieldTypeResponseMapper pivotFieldTypeResponseMapper;
    private final PublishedReportResponseMapper publishedReportResponseMapper;
    private final FolderNodeResponseFolderMapper folderNodeResponseFolderMapper;
    private final ReportFolderRolePermissionMapper reportFolderRolePermissionMapper;
    private final FolderNodeResponseReportFolderMapper folderNodeResponseReportFolderMapper;
    private final ReportCloner reportCloner;
    private final ReportFolderCloner reportFolderCloner;
    private final ReportFolderRoleCloner reportFolderRoleCloner;


    @Value("${magreport.max-level-hierarchy}")
    Long maxLevel;

    @Transactional
    public ReportFolderResponse getFolder(Long userId, Long id) {

        if (id != null) checkFolderExists(id);

        ReportFolderResponse result;

        if (id == null) {
            result = new ReportFolderResponse()
                    .setName("root")
                    .setChildFolders(reportFolderResponseMapper.shallowMap(folderRepository.getAllByParentFolderIsNull()))
                    .setCreated(LocalDateTime.now())
                    .setModified(LocalDateTime.now());
        } else {
            var folder = folderRepository.getReferenceById(id);
            result = reportFolderResponseMapper.from(folder);
        }

        result.getReports().forEach(report -> report.setFavorite(favReportRepository.existsByUserIdAndReportId(userId, report.getId())));

        return result;
    }

    @Transactional
    public ReportFolderResponse addFolder(FolderAddRequest request) {

        var folder = reportFolderMapper.from(request);
        folder = folderRepository.saveAndFlush(folder);

        if (folderRepository.checkRingPath(folder.getId()).size() == maxLevel)
            throw new InvalidParametersException("Invalid parent id folder: path is looped");

        return reportFolderResponseMapper.from(folder);
    }

    @Transactional
    public List<ReportFolderResponse> getChildFolders(Long parentId) {
        return reportFolderResponseMapper.from(folderRepository.getAllByParentFolderId(parentId));
    }

    @Transactional
    public ReportFolderResponse renameFolder(FolderRenameRequest request) {

        checkFolderExists(request.getId());

        var folder = folderRepository.getReferenceById(request.getId());
        folder
                .setName(request.getName())
                .setDescription(request.getDescription());

        folder = folderRepository.saveAndFlush(folder);

        return reportFolderResponseMapper.from(folder);
    }

    @Transactional
    public void deleteFolder(Long id) {

        checkFolderExists(id);
        checkFolderEmpty(id);

        folderRepository.deleteById(id);
    }

    @Transactional
    public List<RoleFolderResponse> getFoldersPermittedToRole(Long roleId) {
        final var folderRoles = reportFolderRoleRepository.getAllByRoleId(roleId);

        return folderRoles.stream().map(folderRole -> new RoleFolderResponse(
                folderRole.getFolder().getId(),
                folderRole.getFolder().getName(),
                folderRole.getPermissions().stream()
                        .map(ReportFolderRolePermission::getAuthority)
                        .max(Comparator.comparing(FolderAuthority::getId))
                        .map(perm -> FolderAuthorityEnum.getById(perm.getId()))
                        .orElse(FolderAuthorityEnum.NONE),
                FolderTypes.REPORT_FOLDER
        )).toList();
    }

    @Transactional
    public void addFolderPermittedToRole(List<Long> folders, Long roleId, List<FolderAuthorityEnum> permissions) {

        var folderRoles = folders.stream().map(folder -> {
            var res = new ReportFolderRole()
                    .setFolder(new ReportFolder(folder))
                    .setRole(new Role(roleId))
                    .setPermissions(reportFolderRolePermissionMapper.from(permissions));
            res.getPermissions().forEach(p -> p.setFolderRole(res));
            return res;
        }).toList();

        reportFolderRoleRepository.saveAll(folderRoles);
    }

    @Transactional
    public void deleteFolderPermittedToRole(List<Long> folderIds, Long roleId) {
        reportFolderRoleRepository.deleteAllByFolderIdInAndRoleId(folderIds, roleId);
    }

    @Transactional
    public void deleteReport(Long id) {

        checkReportExists(id);
        checkActiveJobforReport(id);
        reportRepository.deleteById(id);
    }

    @Transactional
    public void deleteReportToFavorites(UserView currentUser, ReportIdRequest request) {

        favReportRepository.deleteByUserIdAndReportId(currentUser.getId(), request.getId());
    }

    @Transactional
    public Long addReport(UserView currentUser, ReportAddRequest request) {

        checkFolderExists(request.getFolderId());

        var dataSet = dataSetDomainService.getDataSet(request.getDataSetId());
        var columnCounter = new AtomicInteger(1);

        var report = reportMapper.from(request);
        report.setFields(reportFieldMapperDataSet.from(dataSet.getFields()));
        report.getFields().forEach(field -> field.setReport(report).setOrdinal(columnCounter.getAndIncrement()));
        report.setUser(new User(currentUser.getId()));
        reportRepository.save(report);

        return report.getId();
    }

    @Transactional
    public void addReportToFavorites(ReportAddFavoritesRequest request, UserView currentUser) {
        var favReport = new FavReport();

        favReport
                .setReport(new Report(request.getReportId()))
                .setFolder(new Folder(request.getFolderId()))
                .setUser(new User(currentUser.getId()));

        favReportRepository.save(favReport);
    }

    @Transactional
    public ReportResponse getReport(Long id) {
        return reportResponseMapper.from(reportRepository.getReferenceById(id));
    }

    @Transactional
    public PublishedReportResponse getPublishedReportResponse(Long reportId) {
        return publishedReportResponseMapper.from(reportRepository.getReferenceById(reportId));
    }

    @Transactional
    public void editReport(ReportEditRequest request) {

        var report = reportRepository.getReferenceById(request.getId());

        report = reportMerger.merge(report, request);

        report.setModifiedDateTime(LocalDateTime.now());
        reportRepository.save(report);
    }

    @Transactional
    public List<PivotFieldTypeResponse> getPivotFieldTypes() {
        return pivotFieldTypeResponseMapper.from(pivotFieldTypeRepository.findAll());
    }

    @Transactional
    public void deleteFields(Long reportId) {

        reportFieldRepository.deleteAllByReportId(reportId);
    }

    @Transactional
    public List<Long> getDeletedFields(ReportEditRequest request) {
        var report = reportRepository.getReferenceById(request.getId());

        return report.getFields()
                .stream()
                .map(BaseEntity::getId)
                .filter(aLong -> request.getFields().stream().noneMatch(newField -> newField.getId().equals(aLong)))
                .toList();
    }

    @Transactional
    public void deleteFields(List<Long> deletedFields) {
        if (!deletedFields.isEmpty()) reportFieldRepository.deleteAllByIdIn(deletedFields);
    }

    @Transactional
    public void addFields(Long reportId, List<DataSetFieldResponse> addedFields) {

        final var lastField = reportFieldRepository.getFirstByReportIdOrderByOrdinalDesc(reportId);
        var ordinal = new AtomicInteger(lastField == null ? 1 : lastField.getOrdinal());

        final var fields = reportFieldMapperDataSet.from(addedFields);
        fields.forEach(field -> {
            field.setReport(new Report(reportId));
            field.setOrdinal(ordinal.incrementAndGet());
        });

        reportFieldRepository.saveAll(fields);
    }

    @Transactional
    public void updateOrdinalFields(Long reportId, Map<String, Integer> objectFields) {

        var report = reportRepository.getReferenceById(reportId);

        report.getFields().forEach(f -> {
            if (objectFields.containsKey(f.getName().toUpperCase())) {
                f.setOrdinal(objectFields.get(f.getName().toUpperCase()));
            }
        });
        reportRepository.save(report);
    }

    @Transactional
    public ReportFolderResponse changeParentFolder(FolderChangeParentRequest request) {
        final var folder = folderRepository.getReferenceById(request.getId());
        final var parentFolder = request.getParentId() == null ? null : new ReportFolder(request.getParentId());
        folder.setParentFolder(parentFolder);

        folderRepository.save(folder);

        if (folderRepository.checkRingPath(request.getId()).size() == maxLevel)
            throw new InvalidParametersException("Invalid new parent id folder: path is looped");

        return reportFolderResponseMapper.from(folder);
    }

    @Transactional
    public FolderResponse getFavReports(UserView currentUser) {

        final var favReports = favReportRepository.findAllByUserId(currentUser.getId());
        return new FolderResponse()
                .setReports(
                        favReports
                                .stream()
                                .map(favReport -> reportResponseMapper.from(favReport.getReport())
                                        .setPath(getPath(favReport.getFolder()))
                                        .setCreated(favReport.getCreatedDateTime())
                                        .setModified(favReport.getReport().getModifiedDateTime()))
                                .toList());
    }

    @Transactional
    public List<FolderNodeResponse> getPathReport(Long idReport) {
        return getPath(reportRepository.getReferenceById(idReport).getFolder());
    }

    @Transactional
    public List<FolderNodeResponse> getPathToFolder(@NonNull Long folderId) {
        checkFolderExists(folderId);

        return getPath(folderRepository.getReferenceById(folderId));
    }

    @Transactional
    public List<ReportShortResponse> checkReportExistsForDataset(Long dataSetId) {
        var result = reportRepository.findByDataSetId(dataSetId);
        return result.stream().map(reportShortResponseMapper::from).toList();
    }

    @Transactional
    public List<Long> getFolderIds(List<Long> objIds) {
        return reportRepository.getAllByIdIn(objIds)
                .stream()
                .map(report -> report.getFolder().getId())
                .distinct()
                .toList();
    }

    @Transactional
    public void changeFilterInstanceParentFolder(ChangeParentFolderRequest request) {
        final var reports = reportRepository.getAllByIdIn(request.getObjIds());
        final var newFolder = new ReportFolder(request.getDestFolderId());
        reports.forEach(filterInstance -> filterInstance.setFolder(newFolder));
        reportRepository.saveAll(reports);
    }

    @Transactional
    public void copyReport(ChangeParentFolderRequest request, UserView currentUser) {
        final var user = new User(currentUser.getId());
        final var folder = new ReportFolder(request.getDestFolderId());
        final var reports = reportCloner.clone(reportRepository.getAllByIdIn(request.getObjIds()));

        reports.forEach(report -> {
            report.setUser(user);
            report.setFolder(folder);
            report.getFilterReportGroups().forEach(group -> setUser(group, user));
        });

        reportRepository.saveAll(reports);
    }

    @Transactional
    public List<Long> copyReportFolder(CopyFolderRequest request, UserView currentUser) {
        final var user = new User(currentUser.getId());
        final var destFolder = request.getDestFolderId() == null ?
                null :
                folderRepository.getReferenceById(request.getDestFolderId());

        var destFolderRoles = destFolder == null ?
                new ArrayList<ReportFolderRole>() :
                reportFolderRoleCloner.clone(destFolder.getFolderRoles());

        var folderCopyIds = new ArrayList<Long>();

        request.getFolderIds().forEach(f -> {
            final var originalFolder = folderRepository.getReferenceById(f);
            final ReportFolder folderCopy;
            if (request.isInheritParentRights() && request.isInheritRightsRecursive())
                folderCopy = copyFolder(originalFolder, destFolder, user, destFolderRoles);
            else
                folderCopy = copyFolder(originalFolder, destFolder, user, null);

            if (Boolean.TRUE.equals(request.isInheritParentRights())) {
                folderCopy.setFolderRoles(mergeFolderRoles(folderCopy.getFolderRoles(), destFolderRoles));
                folderCopy.getFolderRoles().forEach(fr -> fr.setFolder(folderCopy));
            }

            Long newFolderId = folderRepository.save(folderCopy).getId();
            folderCopyIds.add(newFolderId);
        });
        return folderCopyIds;
    }

    private ReportFolder copyFolder(ReportFolder originalFolder, ReportFolder parentFolder, User currentUser, List<ReportFolderRole> destParentFolderRoles) {

        var folderCopy = reportFolderCloner.clone(originalFolder);

        folderCopy.setParentFolder(parentFolder);
        folderCopy.getReports().forEach(r -> {
            r.setUser(currentUser);
            r.getFilterReportGroups().forEach(group -> setUser(group, currentUser));
        });

        var copyChildFolders = new ArrayList<ReportFolder>();
        originalFolder.getChildFolders().forEach(f -> copyChildFolders.add(copyFolder(f, folderCopy, currentUser, destParentFolderRoles)));
        folderCopy.setChildFolders(copyChildFolders);

        if (destParentFolderRoles != null) {
            folderCopy.setFolderRoles(mergeFolderRoles(folderCopy.getFolderRoles(), destParentFolderRoles));
            folderCopy.getFolderRoles().forEach(f -> f.setFolder(folderCopy));
        }

        return folderCopy;
    }

    private List<ReportFolderRole> mergeFolderRoles(List<ReportFolderRole> current, List<ReportFolderRole> dest) {
        Map<Long, ReportFolderRole> folderRoles = new HashMap<>();

        current.forEach(folderRole -> folderRoles.put(folderRole.getRole().getId(), folderRole));

        dest.forEach(folderRole -> {

            if (folderRoles.containsKey(folderRole.getRole().getId())) {
                var currentFolderRole = folderRoles.get(folderRole.getRole().getId());

                var currentPermissionLevel = currentFolderRole.getPermissions().stream().map(o -> o.getAuthority().getId()).max(Comparator.comparingLong(Long::longValue)).orElse(0L);
                var destPermissionLevel = folderRole.getPermissions().stream().map(o -> o.getAuthority().getId()).max(Comparator.comparingLong(Long::longValue)).orElse(0L);

                if (currentPermissionLevel < destPermissionLevel) {
                    currentFolderRole.setPermissions(folderRole.getPermissions());
                    currentFolderRole.getPermissions().forEach(f -> f.setFolderRole(currentFolderRole));
                }

                folderRoles.put(currentFolderRole.getRole().getId(), currentFolderRole);
            } else
                folderRoles.put(folderRole.getRole().getId(), folderRole);

        });

        return new ArrayList<>(folderRoles.values());
    }

    private void setUser(FilterReportGroup group, User user) {
        group.getChildGroups().forEach(filterGroup -> setUser(filterGroup, user));
        group.getFilterReports().forEach(report -> report.setUser(user));
    }

    private List<FolderNodeResponse> getPath(ReportFolder folder) {

        var result = new LinkedList<FolderNodeResponse>();

        result.add(folderNodeResponseReportFolderMapper.from(folder));

        while (folder.getParentFolder() != null) {
            folder = folder.getParentFolder();
            result.addFirst(folderNodeResponseReportFolderMapper.from(folder));
        }

        return result;
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

    private void checkFolderExists(Long id) {

        if (!isFolderExists(id))
            throw new InvalidParametersException("Report folder with id:" + id + " does not exists.");
    }

    private boolean isFolderExists(Long id) {

        return folderRepository.existsById(id);
    }

    private void checkFolderEmpty(Long id) {
        if (!isFolderEmpty(id))
            throw new InvalidParametersException("Folder with id: " + id + " is not empty.");
    }

    private boolean isFolderEmpty(Long id) {

        return !folderRepository.existsByParentFolderId(id) &&
                !reportRepository.existsByFolderId(id);
    }

    private void checkReportExists(Long id) {
        if (!isReportExists(id))
            throw new InvalidParametersException("FilterInstance with id:" + id + " does not exists.");
    }

    private boolean isReportExists(Long id) {
        return reportRepository.existsById(id);
    }

    private void checkActiveJobforReport(Long id){
        if(!isActiveJobs(id))
            throw new InvalidParametersException("Report with id:" + id + " has active report jobs.");
    }
    private boolean isActiveJobs(Long id){

        return reportRepository.getReferenceById(id).getReportJobs().stream().allMatch(job -> Objects.equals(ReportJobStatusEnum.COMPLETE.getId(), job.getStatus().getId()));
    }
}
