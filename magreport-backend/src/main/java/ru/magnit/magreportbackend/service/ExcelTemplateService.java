package ru.magnit.magreportbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.dto.request.ChangeParentFolderRequest;
import ru.magnit.magreportbackend.dto.request.exceltemplate.ExcelTemplateDeleteRequest;
import ru.magnit.magreportbackend.dto.request.exceltemplate.ExcelTemplateGetRequest;
import ru.magnit.magreportbackend.dto.request.exceltemplate.ExcelTemplateSetDefaultRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportIdRequest;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ExcelTemplateFolderResponse;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ExcelTemplateResponse;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ReportExcelTemplateResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderRoleResponse;
import ru.magnit.magreportbackend.exception.FileSystemException;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.exceltemplate.ExcelTemplateAddRequestMapper;
import ru.magnit.magreportbackend.service.domain.ExcelTemplateDomainService;
import ru.magnit.magreportbackend.service.domain.FolderPermissionsDomainService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExcelTemplateService {

    private final ExcelTemplateDomainService domainService;
    private final FileService fileService;
    private final UserDomainService userDomainService;
    private final FolderPermissionsDomainService folderPermissionsDomainService;
    private final ExcelTemplateAddRequestMapper excelTemplateAddRequestMapper;
    private final PermissionCheckerSystem permissionCheckerSystem;

    @Transactional
    public ExcelTemplateFolderResponse addFolder(FolderAddRequest request) {

        return domainService.addFolder(request);
    }

    @Transactional
    public ExcelTemplateFolderResponse getFolder(FolderRequest request) {

        final var folderResponse = domainService.getFolder(request.getId());

        final var folderIds = folderResponse.getChildFolders().stream().map(ExcelTemplateFolderResponse::getId).collect(Collectors.toList());
        folderIds.add(folderResponse.getId());

        final var userRoles = userDomainService.getUserRoles(userDomainService.getCurrentUser().getName(), null).stream().map(RoleView::getId).toList();
        final var foldersPermissions = folderPermissionsDomainService.getFoldersReportPermissionsForRoles(folderIds, userRoles).stream().collect(Collectors.toMap(FolderRoleResponse::getFolderId, FolderRoleResponse::getAuthority));

        folderResponse.setAuthority(foldersPermissions.getOrDefault(folderResponse.getId(), FolderAuthorityEnum.NONE));
        folderResponse.getChildFolders().forEach(folder -> folder.setAuthority(foldersPermissions.getOrDefault(folderResponse.getId(), FolderAuthorityEnum.NONE)));

        return folderResponse;
    }

    @Transactional
    public List<ExcelTemplateFolderResponse> getChildFolders(FolderRequest request) {

        return domainService.getChildFolders(request.getId());
    }

    @Transactional
    public ExcelTemplateFolderResponse renameFolder(FolderRenameRequest request) {

        return domainService.renameFolder(request);
    }

    @Transactional
    public void deleteFolder(FolderRequest request) {

        domainService.deleteFolder(request.getId());
    }

    @Transactional
    public ExcelTemplateResponse addExcelTemplate(String request, MultipartFile file) {

        final var currentUser = userDomainService.getCurrentUser();
        var excelTemplateAddRequest = excelTemplateAddRequestMapper.from(request);

        ExcelTemplateResponse result = null;

        try {
            result = domainService.addExcelTemplate(currentUser, excelTemplateAddRequest);
            fileService.storeFile(result.getId(), file);
            return result;
        } catch (Exception ex) {
            if (result != null && result.getId() != null)
                domainService.deleteExcelTemplate(result.getId());

            throw new FileSystemException("Error while saving excel template.", ex);
        }
    }

    public byte[] getExcelTemplateFile(ExcelTemplateGetRequest request) {
        Long templateId = request.getId();
        return fileService.retrieveFile(templateId);
    }

    public ExcelTemplateFolderResponse changeParentFolder(FolderChangeParentRequest request) {

        var roles = userDomainService.getUserRoles(userDomainService.getCurrentUser().getName(), null).stream().map(RoleView::getId).toList();

        folderPermissionsDomainService.getExcelTemplateFolderPermissionsForRoles(Collections.singletonList(request.getId()), roles)
                .forEach(f -> {
                    if (f.getAuthority() == FolderAuthorityEnum.NONE)
                        throw new InvalidParametersException(String.format("Move for folder is not available with id %s: Permission denied", f.getFolderId()));

                });

        folderPermissionsDomainService.getExcelTemplateFolderPermissionsForRoles(Collections.singletonList(request.getParentId()), roles)
                .forEach(f -> {
                    if (f.getAuthority() != FolderAuthorityEnum.WRITE)
                        throw new InvalidParametersException("Dest folder is not available: Permission denied");
                });

        return domainService.changeParentFolder(request);
    }

    public void deleteExcelTemplate(ExcelTemplateDeleteRequest request) {
        domainService.deleteExcelTemplate(request.getId());
    }

    public void setDefaultExcelTemplateToReport(ExcelTemplateSetDefaultRequest request) {
        domainService.setDefaultExcelTemplateToReport(request);
    }

    public List<ReportExcelTemplateResponse> getAllReportExcelTemplateToReport(ReportIdRequest request) {
        return domainService.getAllReportExcelTemplateToReport(request);
    }

    public void addExcelTemplateToReport(ExcelTemplateSetDefaultRequest request) {
        domainService.addExcelTemplateToReport(request);
    }

    public void changeExcelTemplateParentFolder(ChangeParentFolderRequest request) {
        permissionCheckerSystem.checkPermissionsOnAllFolders(request, domainService::getFolderIds, folderPermissionsDomainService::getExcelTemplateFolderPermissionsForRoles);
        domainService.changeExcelTemplateParentFolder(request);
    }
}
