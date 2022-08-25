package ru.magnit.magreportbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.user.SystemRoles;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.PermissionCheckRequest;
import ru.magnit.magreportbackend.dto.request.folderreport.FolderPermissionSetRequest;
import ru.magnit.magreportbackend.dto.response.permission.DataSetFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.DataSourceFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.ExcelTemplateFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.FilterInstanceFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.FilterTemplateFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.FolderPermissionCheckResponse;
import ru.magnit.magreportbackend.dto.response.permission.FolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.ReportFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.RolePermissionResponse;
import ru.magnit.magreportbackend.dto.response.permission.SecurityFilterFolderPermissionsResponse;
import ru.magnit.magreportbackend.service.domain.FolderPermissionsDomainService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderPermissionsService {

    private final FolderPermissionsDomainService service;
    private final UserDomainService userDomainService;

    public FolderPermissionsResponse getFolderReportPermissions(FolderRequest request) {

        return service.getFolderReportPermissions(request.getId());
    }

    public FolderPermissionsResponse setFolderReportPermissions(FolderPermissionSetRequest request) {
        final var folders = service.getFolderReportBranch(request.getFolderId());
        folders.add(request.getFolderId());

        service.clearFolderReportsPermissions(folders);
        service.setFolderReportPermissions(folders, request);

        return service.getFolderReportPermissions(request.getFolderId());
    }

    public ReportFolderPermissionsResponse getReportFolderPermissions(FolderRequest request) {

        return service.getReportFolderPermissions(request.getId());
    }

    public ReportFolderPermissionsResponse setReportFolderPermissions(FolderPermissionSetRequest request) {
        final var folders = service.getReportFolderBranch(request.getFolderId());
        folders.add(request.getFolderId());

        service.clearReportFoldersPermissions(folders);
        service.setReportFolderPermissions(folders, request);

        return service.getReportFolderPermissions(request.getFolderId());
    }

    public DataSourceFolderPermissionsResponse getDataSourceFolderPermissions(FolderRequest request) {

        return service.getDataSourceFolderPermissions(request.getId());
    }

    public DataSourceFolderPermissionsResponse setDataSourceFolderPermissions(FolderPermissionSetRequest request) {
        final var folders = service.getDataSourceFolderBranch(request.getFolderId());
        folders.add(request.getFolderId());

        service.clearDataSourceFoldersPermissions(folders);
        service.setDataSourceFoldersPermissions(folders, request);

        return service.getDataSourceFolderPermissions(request.getFolderId());
    }

    public DataSetFolderPermissionsResponse getDataSetFolderPermissions(FolderRequest request) {

        return service.getDataSetFolderPermissions(request.getId());
    }

    public DataSetFolderPermissionsResponse setDataSetFolderPermissions(FolderPermissionSetRequest request) {
        final var folders = service.getDataSetFolderBranch(request.getFolderId());
        folders.add(request.getFolderId());

        service.clearDataSetFoldersPermissions(folders);
        service.setDataSetFoldersPermissions(folders, request);

        return service.getDataSetFolderPermissions(request.getFolderId());
    }

    public ExcelTemplateFolderPermissionsResponse getExcelTemplateFolderPermissions(FolderRequest request) {

        return service.getExcelTemplateFolderPermissions(request.getId());
    }

    public ExcelTemplateFolderPermissionsResponse setExcelTemplateFolderPermissions(FolderPermissionSetRequest request) {
        final var folders = service.getExcelTemplateFolderBranch(request.getFolderId());
        folders.add(request.getFolderId());

        service.clearExcelTemplateFoldersPermissions(folders);
        service.setExcelTemplateFoldersPermissions(folders, request);

        return service.getExcelTemplateFolderPermissions(request.getFolderId());
    }

    public FilterInstanceFolderPermissionsResponse getFilterInstanceFolderPermissions(FolderRequest request) {

        return service.getFilterInstanceFolderPermissions(request.getId());
    }

    public FilterInstanceFolderPermissionsResponse setFilterInstanceFolderPermissions(FolderPermissionSetRequest request) {
        final var folders = service.getFilterInstanceFolderBranch(request.getFolderId());
        folders.add(request.getFolderId());

        service.clearFilterInstanceFoldersPermissions(folders);
        service.setFilterInstanceFoldersPermissions(folders, request);

        return service.getFilterInstanceFolderPermissions(request.getFolderId());
    }

    public FilterTemplateFolderPermissionsResponse getFilterTemplateFolderPermissions(FolderRequest request) {

        return service.getFilterTemplateFolderPermissions(request.getId());
    }

    public FilterTemplateFolderPermissionsResponse setFilterTemplateFolderPermissions(FolderPermissionSetRequest request) {
        final var folders = service.getFilterTemplateFolderBranch(request.getFolderId());
        folders.add(request.getFolderId());

        service.clearFilterTemplateFoldersPermissions(folders);
        service.setFilterTemplateFoldersPermissions(folders, request);

        return service.getFilterTemplateFolderPermissions(request.getFolderId());
    }

    public SecurityFilterFolderPermissionsResponse getSecurityFilterFolderPermissions(FolderRequest request) {

        return service.getSecurityFilterFolderPermissions(request.getId());
    }

    public SecurityFilterFolderPermissionsResponse setSecurityFilterFolderPermissions(FolderPermissionSetRequest request) {
        final var folders = service.getSecurityFilterFolderBranch(request.getFolderId());
        folders.add(request.getFolderId());

        service.clearSecurityFilterFoldersPermissions(folders);
        service.setSecurityFilterFoldersPermissions(folders, request);

        return service.getSecurityFilterFolderPermissions(request.getFolderId());
    }

    public FolderPermissionCheckResponse checkFolderPermission(PermissionCheckRequest request) {
        return switch (request.getFolderType()) {
            case PUBLISHED_REPORT -> checkReportFolderPermission(request.getId());
            case REPORT_FOLDER -> checkFolderReportPermission(request.getId());
            case DATASOURCE -> checkDataSourceFolderPermission(request.getId());
            case DATASET -> checkDataSetFolderPermission(request.getId());
            case EXCEL_TEMPLATE -> checkExcelTemplateFolderPermission(request.getId());
            case FILTER_INSTANCE -> checkFilterInstanceFolderPermission(request.getId());
            case FILTER_TEMPLATE -> checkFilterTemplateFolderPermission(request.getId());
            case SECURITY_FILTER -> checkSecurityFilterFolderPermission(request.getId());
        };
    }

    private FolderPermissionCheckResponse checkFolderReportPermission(long folderId) {
        final var folderPermissions = service.getFolderReportPermissions(folderId);

        return checkFolderPermissions(folderPermissions.rolePermissions(), userDomainService);
    }

    private FolderPermissionCheckResponse checkReportFolderPermission(long folderId) {
        final var folderPermissions = service.getReportFolderPermissions(folderId);

        return checkFolderPermissions(folderPermissions.rolePermissions(), userDomainService);
    }

    private FolderPermissionCheckResponse checkDataSourceFolderPermission(long folderId) {
        final var folderPermissions = service.getDataSourceFolderPermissions(folderId);

        return checkFolderPermissions(folderPermissions.rolePermissions(), userDomainService);
    }

    private FolderPermissionCheckResponse checkDataSetFolderPermission(long folderId) {
        final var folderPermissions = service.getDataSetFolderPermissions(folderId);

        return checkFolderPermissions(folderPermissions.rolePermissions(), userDomainService);
    }

    private FolderPermissionCheckResponse checkExcelTemplateFolderPermission(long folderId) {
        final var folderPermissions = service.getExcelTemplateFolderPermissions(folderId);

        return checkFolderPermissions(folderPermissions.rolePermissions(), userDomainService);
    }

    private FolderPermissionCheckResponse checkFilterInstanceFolderPermission(long folderId) {
        final var folderPermissions = service.getFilterInstanceFolderPermissions(folderId);

        return checkFolderPermissions(folderPermissions.rolePermissions(), userDomainService);
    }

    private FolderPermissionCheckResponse checkFilterTemplateFolderPermission(long folderId) {
        final var folderPermissions = service.getFilterTemplateFolderPermissions(folderId);

        return checkFolderPermissions(folderPermissions.rolePermissions(), userDomainService);
    }

    private FolderPermissionCheckResponse checkSecurityFilterFolderPermission(long folderId) {
        final var folderPermissions = service.getSecurityFilterFolderPermissions(folderId);

        return checkFolderPermissions(folderPermissions.rolePermissions(), userDomainService);
    }

    private FolderPermissionCheckResponse checkFolderPermissions(List<RolePermissionResponse> rolePermissions, UserDomainService userDomainService) {
        final var currentUser = userDomainService.getCurrentUser();
        final var userRoles = userDomainService.getUserRoles(currentUser.getName(), null).stream().map(RoleView::getId).collect(Collectors.toSet());

        if (userRoles.contains(SystemRoles.ADMIN.getId())) return new FolderPermissionCheckResponse(true);

        return new FolderPermissionCheckResponse(rolePermissions.stream().anyMatch(rolePermission -> userRoles.contains(rolePermission.role().getId())));
    }

}
