package ru.magnit.magreportbackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.magnit.magreportbackend.dto.inner.olap.OlapUserRequestLog;
import ru.magnit.magreportbackend.dto.request.olap.OlapConfigRequest;
import ru.magnit.magreportbackend.dto.request.olap.OlapCubeRequest;
import ru.magnit.magreportbackend.dto.request.olap.OlapFieldItemsRequest;
import ru.magnit.magreportbackend.dto.request.olap.ReportOlapConfigAddRequest;
import ru.magnit.magreportbackend.dto.request.olap.ReportOlapConfigRequest;
import ru.magnit.magreportbackend.dto.request.olap.ReportOlapConfigSetShareRequest;
import ru.magnit.magreportbackend.dto.request.olap.UsersReceivedMyJobsRequest;
import ru.magnit.magreportbackend.dto.request.reportjob.ReportJobRequest;
import ru.magnit.magreportbackend.dto.response.ResponseBody;
import ru.magnit.magreportbackend.dto.response.ResponseList;
import ru.magnit.magreportbackend.dto.response.olap.OlapAvailableConfigurationsResponse;
import ru.magnit.magreportbackend.dto.response.olap.OlapConfigResponse;
import ru.magnit.magreportbackend.dto.response.olap.OlapCubeResponse;
import ru.magnit.magreportbackend.dto.response.olap.OlapFieldItemsResponse;
import ru.magnit.magreportbackend.dto.response.olap.ReportOlapConfigResponse;
import ru.magnit.magreportbackend.dto.response.user.UserShortInfoResponse;
import ru.magnit.magreportbackend.service.OlapOutService;
import ru.magnit.magreportbackend.service.OlapService;
import ru.magnit.magreportbackend.util.LogHelper;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Запросы для фронтового движка OLAP")
public class OlapController {

    @Value("${magreport.olap.out-service}")
    boolean outService;

    public static final String OLAP_GET_CUBE = "/api/v1/olap/get-cube";
    public static final String OLAP_GET_FIELD_VALUES = "/api/v1/olap/get-field-values";
    public static final String OLAP_CONFIGURATION_UPDATE = "/api/v1/olap/configuration/update";
    public static final String OLAP_CONFIGURATION_REPORT_ADD = "/api/v1/olap/configuration/report-add";
    public static final String OLAP_CONFIGURATION_REPORT_GET = "/api/v1/olap/configuration/report-get";
    public static final String OLAP_GET_USERS_RECEIVED_MY_JOB = "/api/v1/olap/get-users-received-my-jobs";
    public static final String OLAP_CONFIGURATION_REPORT_SET_DEFAULT = "/api/v1/olap/configuration/set-default";
    public static final String OLAP_CONFIGURATION_DELETE = "/api/v1/olap/configuration/delete";
    public static final String OLAP_CONFIGURATION_REPORT_DELETE = "/api/v1/olap/configuration/report-delete";
    public static final String OLAP_CONFIGURATION_REPORT_SHARED = "/api/v1/olap/configuration/report-share";
    public static final String OLAP_CONFIGURATION_GET_AVAILABLE = "/api/v1/olap/configuration/get-available";
    public static final String OLAP_CONFIGURATION_GET_CURRENT = "/api/v1/olap/configuration/get-current";


    private final OlapService olapService;
    private final OlapOutService olapOutService;

    @Operation(summary = "Получение среза OLAP куба")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = OLAP_GET_CUBE,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<OlapCubeResponse> getCube(
            @RequestBody
            OlapCubeRequest request) throws JsonProcessingException {
        LogHelper.logInfoUserMethodStart();
        LogHelper.logInfoOlapUserRequest(new OlapUserRequestLog(OLAP_GET_CUBE, request));

        ResponseBody<OlapCubeResponse> response;
        if (outService) {
            response = ResponseBody.<OlapCubeResponse>builder()
                    .success(true)
                    .message("")
                    .data(olapOutService.getCube(request))
                    .build();

        } else {
            response = ResponseBody.<OlapCubeResponse>builder()
                    .success(true)
                    .message("")
                    .data(olapService.getCube(request))
                    .build();
        }

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение списка уникальных значений поля с учетом фильтра")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = OLAP_GET_FIELD_VALUES,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<OlapFieldItemsResponse> getFilteredFieldValues(
            @RequestBody
            OlapFieldItemsRequest request) throws JsonProcessingException {
        LogHelper.logInfoUserMethodStart();
        LogHelper.logInfoOlapUserRequest(new OlapUserRequestLog(OLAP_GET_FIELD_VALUES, request));

        ResponseBody<OlapFieldItemsResponse> response;
        if (outService) {
            response = ResponseBody.<OlapFieldItemsResponse>builder()
                    .success(true)
                    .message("")
                    .data(olapOutService.getFieldValues(request))
                    .build();
        } else {
            response = ResponseBody.<OlapFieldItemsResponse>builder()
                    .success(true)
                    .message("")
                    .data(olapService.getFieldValues(request))
                    .build();
        }
        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(description = "Сохранение и привязка конфигурации к отчету/пользователю/заданию")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = OLAP_CONFIGURATION_REPORT_ADD,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ReportOlapConfigResponse> addConfigurationUser(
            @RequestBody
            ReportOlapConfigAddRequest request) throws JsonProcessingException {
        LogHelper.logInfoUserMethodStart();
        LogHelper.logInfoOlapUserRequest(new OlapUserRequestLog(OLAP_CONFIGURATION_UPDATE, request));

        var response = ResponseBody.<ReportOlapConfigResponse>builder()
                .success(true)
                .message("")
                .data(olapService.addOlapReportConfig(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(description = "Получения привязки конфиграции")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = OLAP_CONFIGURATION_REPORT_GET,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ReportOlapConfigResponse> getConfigurationUser(
            @RequestBody
            ReportOlapConfigRequest request) throws JsonProcessingException {
        LogHelper.logInfoUserMethodStart();
        LogHelper.logInfoOlapUserRequest(new OlapUserRequestLog(OLAP_CONFIGURATION_UPDATE, request));

        var response = ResponseBody.<ReportOlapConfigResponse>builder()
                .success(true)
                .message("")
                .data(olapService.getOlapReportConfig(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение списка пользователей, с которыми данный пользователь делился заданиями")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = OLAP_GET_USERS_RECEIVED_MY_JOB,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseList<UserShortInfoResponse> getUsersReceivedMyJobs(
            @RequestBody
            UsersReceivedMyJobsRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<UserShortInfoResponse>builder()
                .success(true)
                .message("")
                .data(olapService.getUsersReceivedMyJobs(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Установка конфигурации по умолчанию")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = OLAP_CONFIGURATION_REPORT_SET_DEFAULT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseList<UserShortInfoResponse> setConfigurationDefault(
            @RequestBody
            ReportOlapConfigRequest request) {
        LogHelper.logInfoUserMethodStart();

        olapService.setDefaultReportConfiguration(request);

        var response = ResponseList.<UserShortInfoResponse>builder()
                .success(true)
                .message("Default configuration set successfully")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Удаление конфигурации")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = OLAP_CONFIGURATION_DELETE,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<OlapConfigResponse> deleteConfiguration(
            @RequestBody
            OlapConfigRequest request) {
        LogHelper.logInfoUserMethodStart();

        olapService.deleteOlapConfig(request);
        var response = ResponseBody.<OlapConfigResponse>builder()
                .success(true)
                .message("Configuration delete successfully")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }


    @Operation(summary = "Удаление привязки конфиграции")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = OLAP_CONFIGURATION_REPORT_DELETE,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ReportOlapConfigResponse> deleteConfigurationUser(
            @RequestBody
            ReportOlapConfigRequest request) {
        LogHelper.logInfoUserMethodStart();


        olapService.deleteOlapReportConfig(request);

        var response = ResponseBody.<ReportOlapConfigResponse>builder()
                .success(true)
                .message("Report configuration delete successfully")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Управление общим доступом к конфигурации")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = OLAP_CONFIGURATION_REPORT_SHARED,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ReportOlapConfigResponse> shareConfigurationUser(
            @RequestBody
            ReportOlapConfigSetShareRequest request) {
        LogHelper.logInfoUserMethodStart();

        olapService.setSharedStatusReportConfiguration(request);

        var response = ResponseBody.<ReportOlapConfigResponse>builder()
                .success(true)
                .message("Report configuration status shared update successfully")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение списка доступных конфигураций пользователю")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = OLAP_CONFIGURATION_GET_AVAILABLE,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<OlapAvailableConfigurationsResponse> getConfigurationsUser(
            @RequestBody ReportJobRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<OlapAvailableConfigurationsResponse>builder()
                .success(true)
                .message("")
                .data(olapService.getAvailableConfigurationsForJob(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }


    @Operation(description = "Получение текущей конфигурации")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = OLAP_CONFIGURATION_GET_CURRENT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ReportOlapConfigResponse> getCurrentConfigurationUser(
            @RequestBody ReportJobRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ReportOlapConfigResponse>builder()
                .success(true)
                .message("")
                .data(olapService.getCurrentConfiguration(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }


}
