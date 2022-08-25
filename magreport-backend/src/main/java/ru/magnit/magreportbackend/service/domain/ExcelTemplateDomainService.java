package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplate;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplateFolder;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplateFolderRole;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplateFolderRolePermission;
import ru.magnit.magreportbackend.domain.excel.ReportExcelTemplate;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthority;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.request.ChangeParentFolderRequest;
import ru.magnit.magreportbackend.dto.request.exceltemplate.ExcelTemplateAddRequest;
import ru.magnit.magreportbackend.dto.request.exceltemplate.ExcelTemplateSetDefaultRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderTypes;
import ru.magnit.magreportbackend.dto.request.report.ReportIdRequest;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ExcelTemplateFolderResponse;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ExcelTemplateResponse;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ReportExcelTemplateResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.dto.response.role.RoleFolderResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.exceltemplate.ExcelTemplateFolderMapper;
import ru.magnit.magreportbackend.mapper.exceltemplate.ExcelTemplateFolderResponseMapper;
import ru.magnit.magreportbackend.mapper.exceltemplate.ExcelTemplateFolderRolePermissionMapper;
import ru.magnit.magreportbackend.mapper.exceltemplate.ExcelTemplateMapper;
import ru.magnit.magreportbackend.mapper.exceltemplate.ExcelTemplateResponseMapper;
import ru.magnit.magreportbackend.mapper.exceltemplate.FolderNodeResponseExcelTemplateFolderMapper;
import ru.magnit.magreportbackend.mapper.exceltemplate.ReportExcelTemplateResponseMapper;
import ru.magnit.magreportbackend.repository.ExcelTemplateFolderRepository;
import ru.magnit.magreportbackend.repository.ExcelTemplateFolderRoleRepository;
import ru.magnit.magreportbackend.repository.ExcelTemplateRepository;
import ru.magnit.magreportbackend.repository.ReportExcelTemplateRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExcelTemplateDomainService {

    private final ExcelTemplateFolderRepository folderRepository;
    private final ExcelTemplateRepository excelTemplateRepository;
    private final ReportExcelTemplateRepository reportExcelTemplateRepository;
    private final ExcelTemplateFolderRoleRepository excelTemplateFolderRoleRepository;
    private final ExcelTemplateFolderMapper excelTemplateFolderMapper;
    private final ExcelTemplateFolderResponseMapper excelTemplateFolderResponseMapper;
    private final ExcelTemplateMapper excelTemplateMapper;
    private final ExcelTemplateResponseMapper excelTemplateResponseMapper;
    private final ReportExcelTemplateResponseMapper reportExcelTemplateResponseMapper;
    private final ExcelTemplateFolderRolePermissionMapper excelTemplateFolderRolePermissionMapper;
    private final FolderNodeResponseExcelTemplateFolderMapper folderNodeResponseExcelTemplateFolderMapper;

    @Value("${magreport.max-level-hierarchy}")
    Long maxLevel;

    @Value(value = "${magreport.excel-template.folder}")
    private String templatesPath;

    @Transactional
    public ExcelTemplateFolderResponse addFolder(FolderAddRequest request) {
        var folder = excelTemplateFolderMapper.from(request);
        folder = folderRepository.saveAndFlush(folder);

        if (folderRepository.checkRingPath(folder.getId()).size() == maxLevel)
            throw new InvalidParametersException("Invalid parent id folder: path is looped");

        return excelTemplateFolderResponseMapper.from(folder);
    }

    @Transactional
    public ExcelTemplateFolderResponse getFolder(Long folderId) {
        if (folderId != null) checkFolderExists(folderId);

        if (folderId == null) {
            return new ExcelTemplateFolderResponse()
                    .setName("root")
                    .setChildFolders(excelTemplateFolderResponseMapper.shallowMap(folderRepository.getAllByParentFolderIsNull()))
                    .setCreated(LocalDateTime.now())
                    .setModified(LocalDateTime.now());
        } else {
            var folder = folderRepository.getReferenceById(folderId);

            return excelTemplateFolderResponseMapper.from(folder);
        }
    }

    @Transactional
    public List<ExcelTemplateFolderResponse> getChildFolders(Long parentId) {
        var folders = folderRepository.getAllByParentFolderId(parentId);

        return excelTemplateFolderResponseMapper.from(folders);
    }

    @Transactional
    public ExcelTemplateFolderResponse renameFolder(FolderRenameRequest request) {
        checkFolderExists(request.getId());

        var folder = folderRepository.getReferenceById(request.getId());

        folder
                .setName(request.getName())
                .setDescription(request.getDescription());

        folder = folderRepository.saveAndFlush(folder);

        return excelTemplateFolderResponseMapper.from(folder);
    }

    @Transactional
    public void deleteFolder(Long folderId) {

        checkFolderExists(folderId);
        checkFolderEmpty(folderId);

        folderRepository.deleteById(folderId);
    }

    @Transactional
    public List<RoleFolderResponse> getFoldersPermittedToRole(Long roleId) {
        final var folderRoles = excelTemplateFolderRoleRepository.getAllByRoleId(roleId);

        return folderRoles.stream().map(folderRole -> new RoleFolderResponse(
                folderRole.getFolder().getId(),
                folderRole.getFolder().getName(),
                folderRole.getPermissions().stream()
                        .map(ExcelTemplateFolderRolePermission::getAuthority)
                        .max(Comparator.comparing(FolderAuthority::getId))
                        .map(perm -> FolderAuthorityEnum.getById(perm.getId()))
                        .orElse(FolderAuthorityEnum.NONE),
                FolderTypes.EXCEL_TEMPLATE
        )).toList();
    }

    @Transactional
    public void addFolderPermittedToRole(List<Long> folders, Long roleId, List<FolderAuthorityEnum> permissions) {

        var folderRoles = folders.stream().map(folder -> {
            var res = new ExcelTemplateFolderRole()
                    .setFolder(new ExcelTemplateFolder(folder))
                    .setRole(new Role(roleId))
                    .setPermissions(excelTemplateFolderRolePermissionMapper.from(permissions));
            res.getPermissions().forEach(p -> p.setFolderRole(res));
            return res;
        }).toList();

        excelTemplateFolderRoleRepository.saveAll(folderRoles);
    }

    @Transactional
    public void deleteFolderPermittedToRole(List<Long> folderIds, Long roleId) {
        excelTemplateFolderRoleRepository.deleteAllByFolderIdInAndRoleId(folderIds,roleId);
    }

    @Transactional
    public ExcelTemplateResponse addExcelTemplate(UserView currentUser, ExcelTemplateAddRequest request) {
        checkFolderExists(request.getFolderId());

        var excelTemplate = excelTemplateMapper.from(request);

        excelTemplate.setUser(new User(currentUser.getId()));
        excelTemplate = excelTemplateRepository.save(excelTemplate);

        return excelTemplateResponseMapper.from(excelTemplate);
    }

    @Transactional
    public void deleteExcelTemplate(Long templateId) {
        if (templateId == 1L)
            throw new InvalidParametersException("Error deleting template: default template");

        excelTemplateRepository.deleteById(templateId);
        reportExcelTemplateRepository.setDefaultExcelTemplateInsteadOfRemote();

    }

    @Transactional
    public String getTemplatePathForReport(long reportId, Long templateId) {

        if (templateId == null) {
            final var defaultReportTemplate = reportExcelTemplateRepository.getTopByReportIdAndIsDefaultIsTrue(reportId);
            templateId = defaultReportTemplate.getExcelTemplate().getId();
        }

        return templatesPath + "/" + templateId + ".xlsm";
    }

    @Transactional
    public ExcelTemplateFolderResponse changeParentFolder(FolderChangeParentRequest request) {
        final var folder = folderRepository.getReferenceById(request.getId());
        final var parentFolder = request.getParentId() == null ? null : new ExcelTemplateFolder(request.getParentId());
        folder.setParentFolder(parentFolder);

        folderRepository.save(folder);

        if (folderRepository.checkRingPath(request.getId()).size() == maxLevel)
            throw new InvalidParametersException("Invalid new parent id folder: path is looped");

        return excelTemplateFolderResponseMapper.from(folder);
    }

    @Transactional
    public void setDefaultExcelTemplateToReport(ExcelTemplateSetDefaultRequest request) {

        var reportExcelTemplates = reportExcelTemplateRepository.findAllByReportId(request.getReportId());
        reportExcelTemplates.forEach(ret -> ret.setIsDefault(ret.getExcelTemplate().getId().equals(request.getExcelTemplateId())));
        reportExcelTemplateRepository.saveAll(reportExcelTemplates);

    }

    @Transactional
    public void setDefaultSystemExcelTemplateToReport(Long reportId) {
        reportExcelTemplateRepository.save(
                new ReportExcelTemplate()
                        .setExcelTemplate(new ExcelTemplate().setId(1L))
                        .setReport(new Report().setId(reportId))
                        .setIsDefault(true)
        );
    }

    @Transactional
    public void removeReportExcelTemplate(Long reportId) {
        reportExcelTemplateRepository.removeReportExcelTemplate(reportId);
    }

    @Transactional
    public List<ReportExcelTemplateResponse> getAllReportExcelTemplateToReport(ReportIdRequest request) {
        return reportExcelTemplateRepository.findAllByReportId(request.getId())
                .stream()
                .map(reportExcelTemplateResponseMapper::from)
                .collect(Collectors.toList());

    }

    @Transactional
    public void addExcelTemplateToReport(ExcelTemplateSetDefaultRequest request) {
        reportExcelTemplateRepository.save(
                new ReportExcelTemplate()
                        .setExcelTemplate(new ExcelTemplate().setId(request.getExcelTemplateId()))
                        .setReport(new Report().setId(request.getReportId()))
                        .setIsDefault(false)
        );
    }

    @Transactional
    public Long getDefaultExcelTemplateToReport(Long reportId) {

        final var defaultReportTemplate = reportExcelTemplateRepository.getTopByReportIdAndIsDefaultIsTrue(reportId);
        return defaultReportTemplate.getExcelTemplate().getId();
    }

    @Transactional
    public List<FolderNodeResponse> getPathToFolder(@NonNull Long folderId) {
        checkFolderExists(folderId);

        return getPath(folderRepository.getReferenceById(folderId));
    }

    @Transactional
    public List<Long> getFolderIds(List<Long> objIds) {
        return excelTemplateRepository.getAllByIdIn(objIds)
                .stream()
                .map(template -> template.getFolder().getId())
                .distinct()
                .toList();
    }

    @Transactional
    public void changeExcelTemplateParentFolder(ChangeParentFolderRequest request) {
        final var excelTemplates = excelTemplateRepository.getAllByIdIn(request.getObjIds());
        final var newFolder = new ExcelTemplateFolder(request.getDestFolderId());
        excelTemplates.forEach(excelTemplate -> excelTemplate.setFolder(newFolder));
        excelTemplateRepository.saveAll(excelTemplates);
    }

    private List<FolderNodeResponse> getPath(ExcelTemplateFolder folder) {
        var result = new LinkedList<FolderNodeResponse>();

        result.add(folderNodeResponseExcelTemplateFolderMapper.from(folder));

        while (folder.getParentFolder() != null) {
            folder = folder.getParentFolder();
            result.addFirst(folderNodeResponseExcelTemplateFolderMapper.from(folder));
        }

        return result;
    }

    private void checkFolderExists(Long id) {

        if (!isFolderExists(id))
            throw new InvalidParametersException("FilterInstance folder with id:" + id + " does not exists.");
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
                !excelTemplateRepository.existsByFolderId(id);
    }
}
