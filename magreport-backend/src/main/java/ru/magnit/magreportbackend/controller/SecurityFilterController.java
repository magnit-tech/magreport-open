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
import ru.magnit.magreportbackend.dto.request.ChangeParentFolderRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.ListValuesCheckRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.request.securityfilter.SecurityFilterAddRequest;
import ru.magnit.magreportbackend.dto.request.securityfilter.SecurityFilterRequest;
import ru.magnit.magreportbackend.dto.request.securityfilter.SecurityFilterSetRoleRequest;
import ru.magnit.magreportbackend.dto.response.ResponseBody;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterFolderResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterRoleSettingsResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterValuesCheckResponse;
import ru.magnit.magreportbackend.service.SecurityFilterService;
import ru.magnit.magreportbackend.util.LogHelper;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Управление фильтрами безопасности")
public class SecurityFilterController {

    public static final String SECURITY_FILTER_GET_FOLDER = "/api/v1/security-filter/get-folder";
    public static final String SECURITY_FILTER_ADD_FOLDER = "/api/v1/security-filter/add-folder";
    public static final String SECURITY_FILTER_RENAME_FOLDER = "/api/v1/security-filter/rename-folder";
    public static final String SECURITY_FILTER_DELETE_FOLDER = "/api/v1/security-filter/delete-folder";
    public static final String SECURITY_FILTER_FOLDER_CHANGE_PARENT = "/api/v1/security-filter/change-parent-folder";
    public static final String SECURITY_FILTER_CHANGE_PARENT_FOLDER = "/api/v1/security-filter/change-folder";
    public static final String SECURITY_FILTER_ADD = "/api/v1/security-filter/add";
    public static final String SECURITY_FILTER_EDIT = "/api/v1/security-filter/edit";
    public static final String SECURITY_FILTER_GET = "/api/v1/security-filter/get";
    public static final String SECURITY_FILTER_DELETE = "/api/v1/security-filter/delete";
    public static final String SECURITY_FILTER_SET_ROLE_SETTINGS = "/api/v1/security-filter/set-role-settings";
    public static final String SECURITY_FILTER_GET_ROLE_SETTINGS = "/api/v1/security-filter/get-role-settings";
    public static final String SECURITY_FILTER_SEARCH = "/api/v1/security-filter/search-filter";

    public static final String SECURITY_FILTER_CHECK_VALUES = "/api/v1/security-filter/check-values";

    private final SecurityFilterService service;

    @Operation(summary = "Получение каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SECURITY_FILTER_GET_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<SecurityFilterFolderResponse> getDataSetFolder(
            @RequestBody
                    FolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<SecurityFilterFolderResponse>builder()
                .success(true)
                .message("")
                .data(service.getFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Смена родителя каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SECURITY_FILTER_FOLDER_CHANGE_PARENT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<SecurityFilterFolderResponse> changeSecurityFilterFolderParent(
            @RequestBody
                    FolderChangeParentRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<SecurityFilterFolderResponse>builder()
                .success(true)
                .message("")
                .data(service.changeParentFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Смена родительского каталога фильтра безопасности")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SECURITY_FILTER_CHANGE_PARENT_FOLDER,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> changeSecurityFilterParentFolder(
        @RequestBody
            ChangeParentFolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        service.changeSecurityFilterParentFolder(request);

        var response = ResponseBody.builder()
            .success(true)
            .message("Objects successfully moved")
            .data(null)
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Добавление нового каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SECURITY_FILTER_ADD_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<SecurityFilterFolderResponse> addDataSetFolder(
            @RequestBody
                    FolderAddRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<SecurityFilterFolderResponse>builder()
                .success(true)
                .message("")
                .data(service.addFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Редактирование наименования и описания каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SECURITY_FILTER_RENAME_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<SecurityFilterFolderResponse> renameDataSetFolder(
            @RequestBody
                    FolderRenameRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<SecurityFilterFolderResponse>builder()
                .success(true)
                .message("")
                .data(service.renameFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Удаление каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SECURITY_FILTER_DELETE_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> deleteDataSetFolder(
            @RequestBody
                    FolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        service.deleteFolder(request);

        var response = ResponseBody.builder()
                .success(true)
                .message("Security filter folder with ID:" + request.getId() + " successfully deleted.")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Добавление нового фильтра безопасности")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SECURITY_FILTER_ADD,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<SecurityFilterResponse> addSecurityFilter(
            @RequestBody
                    SecurityFilterAddRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<SecurityFilterResponse>builder()
                .success(true)
                .message("")
                .data(service.addSecurityFilter(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Редактирование фильтра безопасности")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SECURITY_FILTER_EDIT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<SecurityFilterResponse> editSecurityFilter(
            @RequestBody
                    SecurityFilterAddRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<SecurityFilterResponse>builder()
                .success(true)
                .message("")
                .data(service.editSecurityFilter(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение фильтра безопасности")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SECURITY_FILTER_GET,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<SecurityFilterResponse> getSecurityFilter(
            @RequestBody
                    SecurityFilterRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<SecurityFilterResponse>builder()
                .success(true)
                .message("")
                .data(service.getSecurityFilter(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Удаление фильтра безопасности")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SECURITY_FILTER_DELETE,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> deleteSecurityFilter(
            @RequestBody
                    SecurityFilterRequest request) {

        LogHelper.logInfoUserMethodStart();

        service.deleteSecurityFilter(request);

        var response = ResponseBody.builder()
                .success(true)
                .message("")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Установка значений фильтра безопасности для роли")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SECURITY_FILTER_SET_ROLE_SETTINGS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<SecurityFilterRoleSettingsResponse> setRoleSettings(
            @RequestBody
                    SecurityFilterSetRoleRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<SecurityFilterRoleSettingsResponse>builder()
                .success(true)
                .message("")
                .data(service.setRoleSettings(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение значений фильтра безопасности для ролей")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SECURITY_FILTER_GET_ROLE_SETTINGS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<SecurityFilterRoleSettingsResponse> getRoleSettings(
            @RequestBody
                    SecurityFilterRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<SecurityFilterRoleSettingsResponse>builder()
                .success(true)
                .message("")
                .data(service.getRoleSettings(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Поиск по дереву фильтров безопасности")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SECURITY_FILTER_SEARCH,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FolderSearchResponse> searchSecurityFilter(
            @RequestBody
                    FolderSearchRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FolderSearchResponse>builder()
                .success(true)
                .message("")
                .data(service.searchSecurityFilter(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Проверка значений фильтра")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SECURITY_FILTER_CHECK_VALUES,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<SecurityFilterValuesCheckResponse> checkFilterReportValues(
            @RequestBody
            ListValuesCheckRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<SecurityFilterValuesCheckResponse>builder()
                .success(true)
                .message("")
                .data(service.checkFilterReportValues(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }
}