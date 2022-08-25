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
import ru.magnit.magreportbackend.dto.request.filterinstance.ChildNodesRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.ListValuesCheckRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.ListValuesRequest;
import ru.magnit.magreportbackend.dto.request.filterreport.FilterGroupAddRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportRequest;
import ru.magnit.magreportbackend.dto.response.ResponseBody;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterGroupResponse;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportChildNodesResponse;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportValuesCheckResponse;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportValuesResponse;
import ru.magnit.magreportbackend.service.FilterReportService;
import ru.magnit.magreportbackend.util.LogHelper;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Управление фильтрами отчетов")
public class FilterReportController {

    public static final String REPORT_SET_FILTERS = "/api/v1/report/set-filters";
    public static final String REPORT_GET_FILTERS = "/api/v1/report/get-filters";
    public static final String REPORT_FILTER_GET_VALUES = "/api/v1/report-filter/get-values";
    public static final String REPORT_FILTER_GET_CHILD_NODES = "/api/v1/report-filter/get-child-nodes";
    public static final String REPORT_FILTER_CHECK_VALUES = "/api/v1/report-filter/check-values";

    private final FilterReportService service;

    @Operation(summary = "Добавление фильтров в отчет")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = REPORT_SET_FILTERS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FilterGroupResponse> addFilters(
            @RequestBody
                    FilterGroupAddRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FilterGroupResponse>builder()
                .success(true)
                .message("")
                .data(service.addFilters(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение фильтров отчета")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = REPORT_GET_FILTERS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FilterGroupResponse> getFilters(
            @RequestBody
                    ReportRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FilterGroupResponse>builder()
                .success(true)
                .message("")
                .data(service.getFilters(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение значений фильтра")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = REPORT_FILTER_GET_VALUES,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FilterReportValuesResponse> getFilterReportValues(
            @RequestBody
                    ListValuesRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FilterReportValuesResponse>builder()
                .success(true)
                .message("")
                .data(service.getFilterReportValues(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение дочерних узлов иерархического фильтра")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = REPORT_FILTER_GET_CHILD_NODES,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FilterReportChildNodesResponse> getFilterReportChildNodes(
            @RequestBody
                    ChildNodesRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FilterReportChildNodesResponse>builder()
                .success(true)
                .message("")
                .data(service.getChildNodes(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Проверка значений фильтра")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = REPORT_FILTER_CHECK_VALUES,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FilterReportValuesCheckResponse> checkFilterReportValues(
            @RequestBody
                    ListValuesCheckRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FilterReportValuesCheckResponse>builder()
                .success(true)
                .message("")
                .data(service.checkFilterReportValues(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }
}