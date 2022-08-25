package ru.magnit.magreportbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTaskStatusEnum;
import ru.magnit.magreportbackend.dto.request.schedule.ScheduleAddRequest;
import ru.magnit.magreportbackend.dto.request.schedule.ScheduleCalendarAddRequest;
import ru.magnit.magreportbackend.dto.request.schedule.ScheduleRequest;
import ru.magnit.magreportbackend.dto.request.schedule.ScheduleTaskAddRequest;
import ru.magnit.magreportbackend.dto.request.schedule.ScheduleTaskRequest;
import ru.magnit.magreportbackend.dto.response.ResponseBody;
import ru.magnit.magreportbackend.dto.response.ResponseList;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleResponse;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleTaskResponse;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleTaskShortResponse;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleTypeResponse;
import ru.magnit.magreportbackend.service.ReportJobService;
import ru.magnit.magreportbackend.service.ScheduleService;
import ru.magnit.magreportbackend.service.domain.JobTokenDomainService;
import ru.magnit.magreportbackend.util.LogHelper;
import ru.magnit.magreportbackend.util.StringUtils;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Управление заданиями по расписанию")
public class ScheduleController {

    public static final String SCHEDULE_ADD_CALENDAR_DATE = "/api/v1/schedule/add-date";

    public static final String SCHEDULE_ADD = "/api/v1/schedule/add";
    public static final String SCHEDULE_TASK_ADD = "/api/v1/schedule/task-add";
    public static final String SCHEDULE_GET = "/api/v1/schedule/get";
    public static final String SCHEDULE_TASK_GET = "/api/v1/schedule/task-get";
    public static final String SCHEDULE_GET_TYPES = "/api/v1/schedule/get-types";
    public static final String SCHEDULE_DELETE = "/api/v1/schedule/delete";
    public static final String SCHEDULE_TASK_DELETE = "/api/v1/schedule/task-delete";
    public static final String SCHEDULE_TASK_GET_ALL = "/api/v1/schedule/task-get-all";
    public static final String SCHEDULE_EDIT = "/api/v1/schedule/edit";
    public static final String SCHEDULE_GET_ALL = "/api/v1/schedule/get-all";
    public static final String SCHEDULE_TASK_PROLONGATION = "/api/v1/schedule/prolongation";
    public static final String SCHEDULE_TASK_MANUAL_START = "/api/v1/schedule/task-manual-start";
    public static final String SCHEDULE_TASK_EDIT = "/api/v1/schedule/task-edit";
    public static final String SCHEDULE_TASK_GET_EXCEL_REPORT = "/api/v1/schedule/excel-report/{reportToken}";
    public static final String SCHEDULE_TASK_RUN = "/api/v1/schedule/task-run";
    public static final String SCHEDULE_TASK_SWITCH = "/api/v1/schedule/task-switch";
    public static final String SCHEDULE_TASK_GET_MANUAL_LINK = "/api/v1/schedule/task-get-manual-link";

    private static final String CONTENT_LENGTH = "Content-Length";
    private final ScheduleService service;
    private final ReportJobService reportJobService;
    private final JobTokenDomainService jobTokenService;

    @Value("${magreport.host}")
    private String host;

    @Operation(summary = "Добавление дат в календарь")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SCHEDULE_ADD_CALENDAR_DATE,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<String> addNewDate(
            @RequestBody
                    ScheduleCalendarAddRequest request) {

        LogHelper.logInfoUserMethodStart();

        service.addNewDateScheduleCalendar(request);
        var response = ResponseBody.<String>builder()
                .success(true)
                .message("Добавлено в календарь ")
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Добавление расписания")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SCHEDULE_ADD,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Long> addSchedule(
            @RequestBody
                    ScheduleAddRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<Long>builder()
                .success(true)
                .message("")
                .data(service.addSchedule(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение расписания")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SCHEDULE_GET,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ScheduleResponse> getSchedule(
            @RequestBody
                    ScheduleRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ScheduleResponse>builder()
                .success(true)
                .message("")
                .data(service.getSchedule(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение типов расписаний")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SCHEDULE_GET_TYPES,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseList<ScheduleTypeResponse> getScheduleTypes() {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<ScheduleTypeResponse>builder()
                .success(true)
                .message("")
                .data(service.getScheduleTypes())
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Удаление расписания")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SCHEDULE_DELETE,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ScheduleResponse> deleteSchedule(
            @RequestBody
                    ScheduleRequest request) {

        LogHelper.logInfoUserMethodStart();

        service.deleteSchedule(request);

        var response = ResponseBody.<ScheduleResponse>builder()
                .success(true)
                .message("Schedule with ID:" + request.getId() + " successfully deleted.")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Добавление задачи расписания")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SCHEDULE_TASK_ADD,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Long> addScheduleTask(
            @RequestBody
                    ScheduleTaskAddRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<Long>builder()
                .success(true)
                .message("")
                .data(service.addScheduleTask(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение задачи расписания")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SCHEDULE_TASK_GET,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ScheduleTaskResponse> getScheduleTask(
            @RequestBody
                    ScheduleTaskRequest request) {

        LogHelper.logInfoUserMethodStart();

        var result = service.getScheduleTask(request);

        var response = ResponseBody.<ScheduleTaskResponse>builder()
                .success(true)
                .message("")
                .data(result)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Удаление задачи расписания")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SCHEDULE_TASK_DELETE,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ScheduleTaskResponse> deleteScheduleTask(
            @RequestBody
                    ScheduleTaskRequest request) {

        LogHelper.logInfoUserMethodStart();

        service.deleteScheduleTask(request);

        var response = ResponseBody.<ScheduleTaskResponse>builder()
                .success(true)
                .message("ScheduleTask with ID:" + request.getId() + " successfully deleted.")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение списка всех задач расписаний")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SCHEDULE_TASK_GET_ALL,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseList<ScheduleTaskShortResponse> getAllScheduleTask() {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<ScheduleTaskShortResponse>builder()
                .success(true)
                .message("")
                .data(service.getAllScheduleTask())
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Редактирование расписания")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SCHEDULE_EDIT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ScheduleResponse> editSchedule(
            @RequestBody
                    ScheduleAddRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ScheduleResponse>builder()
                .success(true)
                .message("")
                .data(service.editSchedule(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение списка всех расписаний")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SCHEDULE_GET_ALL,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseList<ScheduleResponse> getAllSchedule() {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<ScheduleResponse>builder()
                .success(true)
                .message("")
                .data(service.getAllSchedules())
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Продление срока выполнения задачи по расписанию")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = SCHEDULE_TASK_PROLONGATION)
    public String prolongationTask(@RequestParam("code") UUID code) {

        LogHelper.logInfoUserMethodStart();

        var response = service.activationExpiredTask(code);

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Ручной запуск задачи по расписанию по коду")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = SCHEDULE_TASK_MANUAL_START)
    public String manualStartSchedule(
            @RequestParam(name = "code") String code ) {

        LogHelper.logInfoUserMethodStart();
        var response = service.startManualTask(code);
        LogHelper.logInfoUserMethodEnd();

        return response;
    }

    @Operation(summary = "Ручной запуск задачи по расписанию по id")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SCHEDULE_TASK_RUN)
    public ResponseBody<ScheduleTaskResponse> manualStartScheduleById(
            @RequestBody
                    ScheduleTaskRequest request) {

        LogHelper.logInfoUserMethodStart();
        service.startRunTask(request);
        LogHelper.logInfoUserMethodEnd();

        return ResponseBody.<ScheduleTaskResponse>builder()
                .success(true)
                .message("Задача поставлена в очередь выполнения")
                .data(null)
                .build();
    }

    @Operation(summary = "Редактирование задачи расписания")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SCHEDULE_TASK_EDIT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ScheduleTaskResponse> editScheduleTask(
            @RequestBody
                    ScheduleTaskAddRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ScheduleTaskResponse>builder()
                .success(true)
                .message("")
                .data(service.editScheduleTask(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение отчета в формате Excel")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = SCHEDULE_TASK_GET_EXCEL_REPORT)
    public ResponseEntity<StreamingResponseBody> getExcelReportGet(
            @PathVariable
                    String reportToken) {
        LogHelper.logInfoUserMethodStart();

        final var value = jobTokenService.getAssociatedValue(reportToken);
        final var jobId = value.getL();
        final var templateId = value.getR();
        final var fileName = reportJobService.getJob(jobId).getReport().name();

        LogHelper.logInfoUserMethodEnd();

        return ResponseEntity
                .ok()
                .header("Access-Control-Request-Headers", CONTENT_LENGTH)
                .header("Access-Control-Expose-Headers", CONTENT_LENGTH)
                .header("Content-Disposition", "attachment; filename*=utf-8''" + StringUtils.getUTF8String(fileName) + ".xlsm")
                .contentLength(reportJobService.getReportSize(jobId, templateId))
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel.sheet.macroEnabled.12"))
                .body(reportJobService.getExcelReport(jobId,templateId));
    }

    @Operation(summary = "Переключение активности задачи расписания")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SCHEDULE_TASK_SWITCH,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ScheduleTaskStatusEnum> switchScheduleTask(
            @RequestBody
                    ScheduleTaskRequest request
    ) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ScheduleTaskStatusEnum>builder()
                .success(true)
                .message("")
                .data(service.switchStatusScheduleTask(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение ссылки на ручной запуск задачи")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SCHEDULE_TASK_GET_MANUAL_LINK,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<String> getLinkManualStart () {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<String>builder()
                .success(true)
                .message("")
                .data(host + SCHEDULE_TASK_MANUAL_START + "?code=")
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

}
