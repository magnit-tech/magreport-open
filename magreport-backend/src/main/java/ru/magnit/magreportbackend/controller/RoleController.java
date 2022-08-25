package ru.magnit.magreportbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.request.role.RolePermissionAddRequest;
import ru.magnit.magreportbackend.dto.request.role.RolePermissionDeleteRequest;
import ru.magnit.magreportbackend.dto.request.user.DomainGroupADRequest;
import ru.magnit.magreportbackend.dto.request.user.RoleAddRequest;
import ru.magnit.magreportbackend.dto.request.user.RoleDomainGroupSetRequest;
import ru.magnit.magreportbackend.dto.request.user.RoleRequest;
import ru.magnit.magreportbackend.dto.request.user.RoleTypeRequest;
import ru.magnit.magreportbackend.dto.request.user.RoleUsersSetRequest;
import ru.magnit.magreportbackend.dto.response.ResponseBody;
import ru.magnit.magreportbackend.dto.response.ResponseList;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResponse;
import ru.magnit.magreportbackend.dto.response.role.RoleFolderPermissionResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterShortResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleDomainGroupResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleTypeResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleUsersResponse;
import ru.magnit.magreportbackend.service.RoleService;
import ru.magnit.magreportbackend.util.LogHelper;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Управление ролями пользователей")
public class RoleController {

    public static final String ROLE_ADD = "/api/v1/role/add";
    public static final String ROLE_GET = "/api/v1/role/get";
    public static final String ROLE_GET_ALL = "/api/v1/role/get-all";
    public static final String ROLE_EDIT = "/api/v1/role/edit";
    public static final String ROLE_DELETE = "/api/v1/role/delete";
    public static final String ROLE_GET_TYPE = "/api/v1/role/get-type";
    public static final String ROLE_GET_ALL_TYPES = "/api/v1/role/get-all-types";
    public static final String ROLE_GET_USERS = "/api/v1/role/get-users";
    public static final String ROLE_SET_USERS = "/api/v1/role/set-users";
    public static final String ROLE_ADD_USERS = "/api/v1/role/add-users";
    public static final String ROLE_DELETE_USERS = "/api/v1/role/delete-users";
    public static final String ROLE_ADD_DOMAIN_GROUPS = "/api/v1/role/add-domain-groups";
    public static final String ROLE_DELETE_DOMAIN_GROUPS = "/api/v1/role/delete-domain-groups";
    public static final String ROLE_GET_DOMAIN_GROUPS = "/api/v1/role/get-domain-groups";
    public static final String AD_GET_DOMAIN_GROUPS = "/api/v1/ad/get-domain-groups";
    public static final String ROLE_SEARCH_ROLES = "/api/v1/role/search-roles";
    public static final String ROLE_GET_PERMITTED_FOLDERS = "/api/v1/admin/role/get-permitted-folders";
    public static final String ROLE_GET_SECURITY_FILTERS = "/api/v1/admin/role/get-security-filters";

    public static final String ROLE_ADD_PERMITTED_FOLDER = "/api/v1/role/admin/add-permission";
    public static final String ROLE_DELETE_PERMITTED_FOLDER = "/api/v1/role/admin/delete-permission";


    private final RoleService service;

    @Operation(summary = "Добавление новой роли")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = ROLE_ADD,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<RoleResponse> addRole(
            @RequestBody
                    RoleAddRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<RoleResponse>builder()
                .success(true)
                .message("")
                .data(service.addRole(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение информации о типе роли")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = ROLE_GET_TYPE,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<RoleTypeResponse> getRoleType(
            @RequestBody
                    RoleTypeRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<RoleTypeResponse>builder()
                .success(true)
                .message("")
                .data(service.getRoleType(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение информации о всех типах ролей")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = ROLE_GET_ALL_TYPES,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseList<RoleTypeResponse> getAllRoleTypes() {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<RoleTypeResponse>builder()
                .success(true)
                .message("")
                .data(service.getAllRoleTypes())
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение информации о роли")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = ROLE_GET,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<RoleResponse> getRole(
            @RequestBody
                    RoleRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<RoleResponse>builder()
                .success(true)
                .message("")
                .data(service.getRole(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение информации о всех ролях")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = ROLE_GET_ALL,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseList<RoleResponse> getAllRoles() {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<RoleResponse>builder()
                .success(true)
                .message("")
                .data(service.getAllRoles())
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Изменение информации о роли")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = ROLE_EDIT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<RoleResponse> editRole(
            @RequestBody
                    RoleAddRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<RoleResponse>builder()
                .success(true)
                .message("")
                .data(service.editRole(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Удаление роли")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = ROLE_DELETE,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> deleteRole(
            @RequestBody
                    RoleRequest request) {

        LogHelper.logInfoUserMethodStart();

        service.deleteRole(request);

        var response = ResponseBody.builder()
                .success(true)
                .message("Role with id= was successfully deleted.")
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение списка пользователей, имеющих роль")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = ROLE_GET_USERS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<RoleUsersResponse> getRoleUsers(
            @RequestBody
                    RoleRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<RoleUsersResponse>builder()
                .success(true)
                .message("")
                .data(service.getRoleUsers(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Установки роли для набора пользователей")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = ROLE_SET_USERS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> setRoleUsers(
            @RequestBody
                    RoleUsersSetRequest request) {

        LogHelper.logInfoUserMethodStart();

        service.setRoleUsers(request);

        var response = ResponseBody.builder()
                .success(true)
                .message("Role was successfully added to users")
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Добавление пользователей в указанную роль")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = ROLE_ADD_USERS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<RoleUsersResponse> addRoleUsers(
            @RequestBody
                    RoleUsersSetRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<RoleUsersResponse>builder()
                .success(true)
                .message("")
                .data(service.addRoleUsers(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }


    @Operation(summary = "Добавление доменных групп в указанную роль")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = ROLE_ADD_DOMAIN_GROUPS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<RoleDomainGroupResponse> addRoleDomainGroups(
            @RequestBody
                    RoleDomainGroupSetRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<RoleDomainGroupResponse>builder()
                .success(true)
                .message("")
                .data(service.addRoleDomainGroups(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Удаление доменных групп из указанной роли")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = ROLE_DELETE_DOMAIN_GROUPS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<RoleDomainGroupResponse> deleteRoleDomainGroups(
            @RequestBody
                    RoleDomainGroupSetRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<RoleDomainGroupResponse>builder()
                .success(true)
                .message("")
                .data(service.deleteRoleDomainGroups(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение доменных групп указанной роли")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = ROLE_GET_DOMAIN_GROUPS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<RoleDomainGroupResponse> getRoleDomainGroups(
            @RequestBody
                    RoleRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<RoleDomainGroupResponse>builder()
                .success(true)
                .message("")
                .data(service.getRoleDomainGroups(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение доменных групп из AD по подстроке")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = AD_GET_DOMAIN_GROUPS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseList<String> getADDomainGroups(
            @RequestBody
                    DomainGroupADRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<String>builder()
                .success(true)
                .message("")
                .data(service.getADDomainGroups(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Удаление пользователей из указанной роли")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = ROLE_DELETE_USERS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<RoleUsersResponse> deleteRoleUsers(
            @RequestBody
                    RoleUsersSetRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<RoleUsersResponse>builder()
                .success(true)
                .message("")
                .data(service.deleteRoleUsers(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Поиск по дереву ролей")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = ROLE_SEARCH_ROLES,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FolderSearchResponse> searchRoles(
            @RequestBody
                    FolderSearchRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FolderSearchResponse>builder()
                .success(true)
                .message("")
                .data(service.searchRole(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение списка каталогов, к которым роль имеет доступ")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = ROLE_GET_PERMITTED_FOLDERS,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseBody<RoleFolderPermissionResponse> getFoldersWithPermissions(
        @RequestBody
            RoleRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<RoleFolderPermissionResponse>builder()
            .success(true)
            .message("")
            .data(service.getPermittedFolders(request))
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение фильтров безопасности, для которых роль имеет настройки")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = ROLE_GET_SECURITY_FILTERS,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseList<SecurityFilterShortResponse> getSecurityFiltersWithSettings(
        @RequestBody
            RoleRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<SecurityFilterShortResponse>builder()
            .success(true)
            .message("")
            .data(service.getSecurityFilters(request))
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Добавление доступа роли к каталогу")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = ROLE_ADD_PERMITTED_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> addFolderPermission(
            @RequestBody
                    RolePermissionAddRequest request) {

        LogHelper.logInfoUserMethodStart();
        service.addFolderPermission(request);
        var response = ResponseBody.builder()
                .success(true)
                .message("Folder permission was successfully added to role")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Удаление доступа роли к каталогу")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = ROLE_DELETE_PERMITTED_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> deleteFolderPermission(
            @RequestBody
                    RolePermissionDeleteRequest request) {

        LogHelper.logInfoUserMethodStart();
        service.deleteFolderPermission(request);
        var response = ResponseBody.builder()
                .success(true)
                .message("Folder permission was successfully deleted to role")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }
}