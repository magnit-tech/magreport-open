package ru.magnit.magreportbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.request.role.RolePermissionAddRequest;
import ru.magnit.magreportbackend.dto.request.role.RolePermissionDeleteRequest;
import ru.magnit.magreportbackend.dto.request.user.DomainGroupADRequest;
import ru.magnit.magreportbackend.dto.request.user.RoleAddRequest;
import ru.magnit.magreportbackend.dto.request.user.RoleDomainGroupSetRequest;
import ru.magnit.magreportbackend.dto.request.user.RoleRequest;
import ru.magnit.magreportbackend.dto.request.user.RoleTypeRequest;
import ru.magnit.magreportbackend.dto.request.user.RoleUsersSetRequest;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResponse;
import ru.magnit.magreportbackend.dto.response.role.RoleFolderPermissionResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterShortResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleDomainGroupResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleTypeResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleUsersResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.service.domain.DataSetDomainService;
import ru.magnit.magreportbackend.service.domain.DataSourceDomainService;
import ru.magnit.magreportbackend.service.domain.ExcelTemplateDomainService;
import ru.magnit.magreportbackend.service.domain.FilterInstanceDomainService;
import ru.magnit.magreportbackend.service.domain.FilterTemplateDomainService;
import ru.magnit.magreportbackend.service.domain.FolderDomainService;
import ru.magnit.magreportbackend.service.domain.FolderPermissionsDomainService;
import ru.magnit.magreportbackend.service.domain.LdapService;
import ru.magnit.magreportbackend.service.domain.ReportDomainService;
import ru.magnit.magreportbackend.service.domain.RoleDomainService;
import ru.magnit.magreportbackend.service.domain.SecurityFilterDomainService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleDomainService service;
    private final LdapService ldapService;
    private final FilterTemplateDomainService filterTemplateDomainService;
    private final ExcelTemplateDomainService excelTemplateDomainService;
    private final FilterInstanceDomainService filterInstanceDomainService;
    private final SecurityFilterDomainService securityFilterDomainService;
    private final DataSourceDomainService dataSourceDomainService;
    private final DataSetDomainService dataSetDomainService;
    private final ReportDomainService reportDomainService;
    private final FolderDomainService folderDomainService;
    private final FolderPermissionsDomainService folderPermissionsDomainService;

    public RoleResponse addRole(RoleAddRequest request) {

        var roleId = service.saveRole(request);

        return roleId == null ? null : service.getRole(roleId);
    }

    public RoleTypeResponse getRoleType(RoleTypeRequest request) {

        return service.getRoleType(request.getId());
    }

    public RoleResponse getRole(RoleRequest request) {

        return service.getRole(request.getId());
    }

    public void deleteRole(RoleRequest request) {
        final var permittedFolders = getPermittedFolders(request);
        final var secFilters = getSecurityFilters(request);
        final var domainGroups = getRoleDomainGroups(request);
        final var roleUsers = getRoleUsers(request);

        if (!permittedFolders.isEmpty() || !secFilters.isEmpty() || !domainGroups.isEmpty() || !roleUsers.isEmpty()) {
            throw new InvalidParametersException(getRoleDependenciesDescription(permittedFolders, secFilters, domainGroups, roleUsers));
        }

        service.deleteRole(request.getId());
    }

    public RoleUsersResponse getRoleUsers(RoleRequest request) {

        return service.getRoleUsers(request.getId());
    }

    public void setRoleUsers(RoleUsersSetRequest request) {

        service.clearUsersFromRole(request.getId());
        service.setRoleUsers(request);
    }

    public RoleResponse editRole(RoleAddRequest request) {

        var roleId = service.updateRole(request);

        return service.getRole(roleId);
    }

    public RoleUsersResponse addRoleUsers(RoleUsersSetRequest request) {

        service.addUserToRole(request.getId(), request.getUsers());

        return service.getRoleUsers(request.getId());
    }

    public RoleUsersResponse deleteRoleUsers(RoleUsersSetRequest request) {

        service.clearUsersFromRole(request.getId(), request.getUsers());

        return service.getRoleUsers(request.getId());
    }

    public RoleDomainGroupResponse addRoleDomainGroups(RoleDomainGroupSetRequest request) {

        service.addDomainGroups(request.getDomainGroups());

        service.addRoleDomainGroups(request);

        return service.getRoleDomainGroups(request.getId());
    }

    public RoleDomainGroupResponse deleteRoleDomainGroups(RoleDomainGroupSetRequest request) {

        service.deleteRoleDomainGroups(request);

        return service.getRoleDomainGroups(request.getId());
    }

    public RoleDomainGroupResponse getRoleDomainGroups(RoleRequest request) {

        return service.getRoleDomainGroups(request.getId());
    }

    public List<RoleResponse> getAllRoles() {

        return service.getAllRoles();
    }

    public List<RoleTypeResponse> getAllRoleTypes() {
        return service.getAllRoleTypes();
    }

    public List<String> getADDomainGroups(DomainGroupADRequest request) {

        return ldapService.getGroupsByNamePart(request.getNamePart());
    }

    public void deleteRole(String roleName) {
        service.deleteRole(roleName);
    }

    public RoleResponse getRoleByName(String roleName) {
        return service.getRoleByName(roleName);
    }

    public FolderSearchResponse searchRole(FolderSearchRequest request) {
        return service.searchRole(request);
    }

    public RoleFolderPermissionResponse getPermittedFolders(RoleRequest request) {
        return new RoleFolderPermissionResponse(
                filterTemplateDomainService.getFoldersPermittedToRole(request.getId()),
                excelTemplateDomainService.getFoldersPermittedToRole(request.getId()),
                filterInstanceDomainService.getFoldersPermittedToRole(request.getId()),
                securityFilterDomainService.getFoldersPermittedToRole(request.getId()),
                dataSourceDomainService.getFoldersPermittedToRole(request.getId()),
                dataSetDomainService.getFoldersPermittedToRole(request.getId()),
                reportDomainService.getFoldersPermittedToRole(request.getId()),
                folderDomainService.getFoldersPermittedToRole(request.getId())
        );
    }

    public List<SecurityFilterShortResponse> getSecurityFilters(RoleRequest request) {
        return securityFilterDomainService.getFiltersWithSettingsForRole(request.getId());
    }

    public void addFolderPermission(RolePermissionAddRequest request) {
        switch (request.getType()) {
            case PUBLISHED_REPORT -> {
                var folders = folderPermissionsDomainService.getFolderReportBranch(request.getFolderId());
                folders.add(request.getFolderId());
                folderDomainService.deleteFolderPermittedToRole(folders, request.getRoleId());
                folderDomainService.addFolderPermittedToRole(folders, request.getRoleId(), request.getPermissions());
            }
            case REPORT_FOLDER -> {
                var folders = folderPermissionsDomainService.getReportFolderBranch(request.getFolderId());
                folders.add(request.getFolderId());
                reportDomainService.deleteFolderPermittedToRole(folders, request.getRoleId());
                reportDomainService.addFolderPermittedToRole(folders, request.getRoleId(), request.getPermissions());
            }
            case DATASOURCE -> {
                var folders = folderPermissionsDomainService.getDataSourceFolderBranch(request.getFolderId());
                folders.add(request.getFolderId());
                dataSourceDomainService.deleteFolderPermittedToRole(folders, request.getRoleId());
                dataSourceDomainService.addFolderPermittedToRole(folders, request.getRoleId(), request.getPermissions());
            }
            case DATASET -> {
                var folders = folderPermissionsDomainService.getDataSetFolderBranch(request.getFolderId());
                folders.add(request.getFolderId());
                dataSetDomainService.deleteFolderPermittedToRole(folders, request.getRoleId());
                dataSetDomainService.addFolderPermittedToRole(folders, request.getRoleId(), request.getPermissions());
            }
            case EXCEL_TEMPLATE -> {
                var folders = folderPermissionsDomainService.getExcelTemplateFolderBranch(request.getFolderId());
                folders.add(request.getFolderId());
                excelTemplateDomainService.deleteFolderPermittedToRole(folders, request.getRoleId());
                excelTemplateDomainService.addFolderPermittedToRole(folders, request.getRoleId(), request.getPermissions());
            }
            case FILTER_INSTANCE -> {
                var folders = folderPermissionsDomainService.getFilterInstanceFolderBranch(request.getFolderId());
                folders.add(request.getFolderId());
                filterInstanceDomainService.deleteFolderPermittedToRole(folders, request.getRoleId());
                filterInstanceDomainService.addFolderPermittedToRole(folders, request.getRoleId(), request.getPermissions());
            }
            case FILTER_TEMPLATE -> {
                var folders = folderPermissionsDomainService.getFilterTemplateFolderBranch(request.getFolderId());
                folders.add(request.getFolderId());
                filterTemplateDomainService.deleteFolderPermittedToRole(folders, request.getRoleId());
                filterTemplateDomainService.addFolderPermittedToRole(folders, request.getRoleId(), request.getPermissions());
            }
            case SECURITY_FILTER -> {
                var folders = folderPermissionsDomainService.getSecurityFilterFolderBranch(request.getFolderId());
                folders.add(request.getFolderId());
                securityFilterDomainService.deleteFolderPermittedToRole(folders, request.getRoleId());
                securityFilterDomainService.addFolderPermittedToRole(folders, request.getRoleId(), request.getPermissions());
            }
        }

    }

    public void deleteFolderPermission(RolePermissionDeleteRequest request) {
        switch (request.getType()) {
            case PUBLISHED_REPORT -> {
                var folders = folderPermissionsDomainService.getFolderReportBranch(request.getFolderId());
                folders.add(request.getFolderId());
                folderDomainService.deleteFolderPermittedToRole(folders, request.getRoleId());
            }
            case REPORT_FOLDER -> {
                var folders = folderPermissionsDomainService.getReportFolderBranch(request.getFolderId());
                folders.add(request.getFolderId());
                reportDomainService.deleteFolderPermittedToRole(folders, request.getRoleId());
            }
            case DATASOURCE -> {
                var folders = folderPermissionsDomainService.getDataSourceFolderBranch(request.getFolderId());
                folders.add(request.getFolderId());
                dataSourceDomainService.deleteFolderPermittedToRole(folders, request.getRoleId());
            }
            case DATASET -> {
                var folders = folderPermissionsDomainService.getDataSetFolderBranch(request.getFolderId());
                folders.add(request.getFolderId());
                dataSetDomainService.deleteFolderPermittedToRole(folders, request.getRoleId());
            }
            case EXCEL_TEMPLATE -> {
                var folders = folderPermissionsDomainService.getExcelTemplateFolderBranch(request.getFolderId());
                folders.add(request.getFolderId());
                excelTemplateDomainService.deleteFolderPermittedToRole(folders, request.getRoleId());
            }
            case FILTER_INSTANCE -> {
                var folders = folderPermissionsDomainService.getFilterInstanceFolderBranch(request.getFolderId());
                folders.add(request.getFolderId());
                filterInstanceDomainService.deleteFolderPermittedToRole(folders, request.getRoleId());
            }
            case FILTER_TEMPLATE -> {

                var folders = folderPermissionsDomainService.getFilterTemplateFolderBranch(request.getFolderId());
                folders.add(request.getFolderId());
                filterTemplateDomainService.deleteFolderPermittedToRole(folders, request.getRoleId());
            }
            case SECURITY_FILTER -> {
                var folders = folderPermissionsDomainService.getSecurityFilterFolderBranch(request.getFolderId());
                folders.add(request.getFolderId());
                securityFilterDomainService.deleteFolderPermittedToRole(folders, request.getRoleId());
            }
        }
    }


    private String getRoleDependenciesDescription(
            RoleFolderPermissionResponse permittedFolders,
            List<SecurityFilterShortResponse> secFilters,
            RoleDomainGroupResponse domainGroups,
            RoleUsersResponse roleUsers) {

        final var resultBuilder = new StringBuilder();
        resultBuilder.append("Роль не может быть удалена из-за наличия связанных объектов:\n");
        if (!permittedFolders.isEmpty()) {
            resultBuilder.append("\tПрава доступа к каталогам:\n");
            permittedFolders.allRoleFolders().forEach(folderGroup -> {
                resultBuilder.append("\t\t").append(folderGroup.getL()).append(":\n");
                folderGroup.getR().forEach(folder ->
                        resultBuilder
                                .append("\t\t\t")
                                .append(folder.folderId())
                                .append(": ")
                                .append(folder.folderName())
                                .append("\n"));
            });
        }
        if (!secFilters.isEmpty()) {
            resultBuilder.append("\tНастройки фильтров безопасности:\n");
            secFilters.forEach(filter ->
                    resultBuilder
                            .append("\t\t")
                            .append(filter.id())
                            .append(": ")
                            .append(filter.name())
                            .append("\n"));
        }
        if (!domainGroups.isEmpty()) {
            resultBuilder.append("\tНастройки доменных групп:\n");
            secFilters.forEach(group ->
                    resultBuilder
                            .append("\t\t")
                            .append(group.name())
                            .append("\n"));
        }
        if (!roleUsers.isEmpty()) {
            resultBuilder.append("\tПользователи, включенные в роль:\n");
            roleUsers.getUsers().forEach(user ->
                    resultBuilder
                            .append("\t\t")
                            .append(user.getName())
                            .append("\n"));
        }

        return resultBuilder.toString();
    }
}
