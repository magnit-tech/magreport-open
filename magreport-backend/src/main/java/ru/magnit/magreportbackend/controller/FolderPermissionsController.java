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
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.PermissionCheckRequest;
import ru.magnit.magreportbackend.dto.request.folderreport.FolderPermissionSetRequest;
import ru.magnit.magreportbackend.dto.response.ResponseBody;
import ru.magnit.magreportbackend.dto.response.permission.DataSetFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.DataSourceFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.ExcelTemplateFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.FilterInstanceFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.FilterTemplateFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.FolderPermissionCheckResponse;
import ru.magnit.magreportbackend.dto.response.permission.FolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.ReportFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.SecurityFilterFolderPermissionsResponse;
import ru.magnit.magreportbackend.service.FolderPermissionsService;
import ru.magnit.magreportbackend.util.LogHelper;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Управление правами доступа к каталогам")
public class FolderPermissionsController {


    public static final String FOLDER_PERMISSION_CHECK = "/api/v1/user-services/check-permission";
    public static final String FOLDER_ADMIN_SET_PERMISSIONS = "/api/v1/folder/admin/set-permissions";
    public static final String FOLDER_ADMIN_GET_PERMISSIONS = "/api/v1/folder/admin/get-permissions";
    public static final String REPORT_FOLDER_ADMIN_SET_PERMISSIONS = "/api/v1/report/admin/set-permissions";
    public static final String REPORT_FOLDER_ADMIN_GET_PERMISSIONS = "/api/v1/report/admin/get-permissions";
    public static final String DATASOURCE_FOLDER_ADMIN_SET_PERMISSIONS = "/api/v1/datasource/admin/set-permissions";
    public static final String DATASOURCE_FOLDER_ADMIN_GET_PERMISSIONS = "/api/v1/datasource/admin/get-permissions";
    public static final String DATASET_FOLDER_ADMIN_SET_PERMISSIONS = "/api/v1/dataset/admin/set-permissions";
    public static final String DATASET_FOLDER_ADMIN_GET_PERMISSIONS = "/api/v1/dataset/admin/get-permissions";
    public static final String EXCEL_TEMPLATE_FOLDER_ADMIN_SET_PERMISSIONS = "/api/v1/excel-template/admin/set-permissions";
    public static final String EXCEL_TEMPLATE_FOLDER_ADMIN_GET_PERMISSIONS = "/api/v1/excel-template/admin/get-permissions";
    public static final String FILTER_INSTANCE_FOLDER_ADMIN_SET_PERMISSIONS = "/api/v1/filter-instance/admin/set-permissions";
    public static final String FILTER_INSTANCE_FOLDER_ADMIN_GET_PERMISSIONS = "/api/v1/filter-instance/admin/get-permissions";
    public static final String FILTER_TEMPLATE_FOLDER_ADMIN_SET_PERMISSIONS = "/api/v1/filter-template/admin/set-permissions";
    public static final String FILTER_TEMPLATE_FOLDER_ADMIN_GET_PERMISSIONS = "/api/v1/filter-template/admin/get-permissions";
    public static final String SECURITY_FILTER_FOLDER_ADMIN_SET_PERMISSIONS = "/api/v1/security-filter/admin/set-permissions";
    public static final String SECURITY_FILTER_FOLDER_ADMIN_GET_PERMISSIONS = "/api/v1/security-filter/admin/get-permissions";

    private final FolderPermissionsService service;

    @Operation(summary = "Получение настроек доступа к каталогу опубликованных отчетов")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FOLDER_ADMIN_GET_PERMISSIONS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FolderPermissionsResponse> getFolderReportPermissions(
            @RequestBody
                    FolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FolderPermissionsResponse>builder()
                .success(true)
                .message("")
                .data(service.getFolderReportPermissions(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Установка настроек доступа к каталогу опубликованных отчетов")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FOLDER_ADMIN_SET_PERMISSIONS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FolderPermissionsResponse> setFolderReportPermissions(
            @RequestBody
                    FolderPermissionSetRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FolderPermissionsResponse>builder()
                .success(true)
                .message("")
                .data(service.setFolderReportPermissions(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение настроек доступа к каталогу исходных отчетов")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = REPORT_FOLDER_ADMIN_GET_PERMISSIONS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ReportFolderPermissionsResponse> getReportFolderPermissions(
            @RequestBody
                    FolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ReportFolderPermissionsResponse>builder()
                .success(true)
                .message("")
                .data(service.getReportFolderPermissions(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Установка настроек доступа к каталогу исходных отчетов")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = REPORT_FOLDER_ADMIN_SET_PERMISSIONS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ReportFolderPermissionsResponse> setReportFolderPermissions(
            @RequestBody
                    FolderPermissionSetRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ReportFolderPermissionsResponse>builder()
                .success(true)
                .message("")
                .data(service.setReportFolderPermissions(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение настроек доступа к каталогу источников данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASOURCE_FOLDER_ADMIN_GET_PERMISSIONS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<DataSourceFolderPermissionsResponse> getDataSourceFolderPermissions(
            @RequestBody
                    FolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<DataSourceFolderPermissionsResponse>builder()
                .success(true)
                .message("")
                .data(service.getDataSourceFolderPermissions(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Установка настроек доступа к каталогу источников данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASOURCE_FOLDER_ADMIN_SET_PERMISSIONS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<DataSourceFolderPermissionsResponse> setDataSourceFolderPermissions(
            @RequestBody
                    FolderPermissionSetRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<DataSourceFolderPermissionsResponse>builder()
                .success(true)
                .message("")
                .data(service.setDataSourceFolderPermissions(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение настроек доступа к каталогу наборов данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASET_FOLDER_ADMIN_GET_PERMISSIONS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<DataSetFolderPermissionsResponse> getDataSetFolderPermissions(
            @RequestBody
                    FolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<DataSetFolderPermissionsResponse>builder()
                .success(true)
                .message("")
                .data(service.getDataSetFolderPermissions(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Установка настроек доступа к каталогу наборов данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASET_FOLDER_ADMIN_SET_PERMISSIONS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<DataSetFolderPermissionsResponse> setDataSetFolderPermissions(
            @RequestBody
                    FolderPermissionSetRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<DataSetFolderPermissionsResponse>builder()
                .success(true)
                .message("")
                .data(service.setDataSetFolderPermissions(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение настроек доступа к каталогу шаблонов отчетов в формате Excel")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = EXCEL_TEMPLATE_FOLDER_ADMIN_GET_PERMISSIONS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ExcelTemplateFolderPermissionsResponse> getExcelTemplateFolderPermissions(
            @RequestBody
                    FolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ExcelTemplateFolderPermissionsResponse>builder()
                .success(true)
                .message("")
                .data(service.getExcelTemplateFolderPermissions(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Установка настроек доступа к каталогу шаблонов отчетов в формате Excel")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = EXCEL_TEMPLATE_FOLDER_ADMIN_SET_PERMISSIONS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ExcelTemplateFolderPermissionsResponse> setExcelTemplateFolderPermissions(
            @RequestBody
                    FolderPermissionSetRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ExcelTemplateFolderPermissionsResponse>builder()
                .success(true)
                .message("")
                .data(service.setExcelTemplateFolderPermissions(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение настроек доступа к каталогу экземпляров фильтров")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_INSTANCE_FOLDER_ADMIN_GET_PERMISSIONS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FilterInstanceFolderPermissionsResponse> getFilterInstanceFolderPermissions(
            @RequestBody
                    FolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FilterInstanceFolderPermissionsResponse>builder()
                .success(true)
                .message("")
                .data(service.getFilterInstanceFolderPermissions(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Установка настроек доступа к каталогу экземпляров фильтров")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_INSTANCE_FOLDER_ADMIN_SET_PERMISSIONS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FilterInstanceFolderPermissionsResponse> setFilterInstanceFolderPermissions(
            @RequestBody
                    FolderPermissionSetRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FilterInstanceFolderPermissionsResponse>builder()
                .success(true)
                .message("")
                .data(service.setFilterInstanceFolderPermissions(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение настроек доступа к каталогу шаблонов фильтров")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_TEMPLATE_FOLDER_ADMIN_GET_PERMISSIONS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FilterTemplateFolderPermissionsResponse> getFilterTemplateFolderPermissions(
            @RequestBody
                    FolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FilterTemplateFolderPermissionsResponse>builder()
                .success(true)
                .message("")
                .data(service.getFilterTemplateFolderPermissions(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Установка настроек доступа к каталогу шаблонов фильтров")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_TEMPLATE_FOLDER_ADMIN_SET_PERMISSIONS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FilterTemplateFolderPermissionsResponse> setFilterTemplateFolderPermissions(
            @RequestBody
                    FolderPermissionSetRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FilterTemplateFolderPermissionsResponse>builder()
                .success(true)
                .message("")
                .data(service.setFilterTemplateFolderPermissions(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение настроек доступа к каталогу фильтров безопасности")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SECURITY_FILTER_FOLDER_ADMIN_GET_PERMISSIONS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<SecurityFilterFolderPermissionsResponse> getSecurityFilterFolderPermissions(
            @RequestBody
                    FolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<SecurityFilterFolderPermissionsResponse>builder()
                .success(true)
                .message("")
                .data(service.getSecurityFilterFolderPermissions(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Установка настроек доступа к каталогу фильтров безопасности")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SECURITY_FILTER_FOLDER_ADMIN_SET_PERMISSIONS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<SecurityFilterFolderPermissionsResponse> setSecurityFilterFolderPermissions(
            @RequestBody
                    FolderPermissionSetRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<SecurityFilterFolderPermissionsResponse>builder()
                .success(true)
                .message("")
                .data(service.setSecurityFilterFolderPermissions(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Проверка доступа к каталогу")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FOLDER_PERMISSION_CHECK,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FolderPermissionCheckResponse> folderPermissionCheck(
            @RequestBody
                    PermissionCheckRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FolderPermissionCheckResponse>builder()
                .success(true)
                .message("")
                .data(service.checkFolderPermission(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }
}
