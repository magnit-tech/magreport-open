package ru.magnit.magreportbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTaskStatusEnum;
import ru.magnit.magreportbackend.dto.request.report.ScheduleReportRequest;
import ru.magnit.magreportbackend.dto.request.schedule.ScheduleAddRequest;
import ru.magnit.magreportbackend.dto.request.schedule.ScheduleCalendarAddRequest;
import ru.magnit.magreportbackend.dto.request.schedule.ScheduleRequest;
import ru.magnit.magreportbackend.dto.request.schedule.ScheduleTaskAddRequest;
import ru.magnit.magreportbackend.dto.request.schedule.ScheduleTaskRequest;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportFieldResponse;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportResponse;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleResponse;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleShortResponse;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleTaskResponse;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleTaskShortResponse;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleTypeResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.service.domain.DestinationDomainService;
import ru.magnit.magreportbackend.service.domain.ReportDomainService;
import ru.magnit.magreportbackend.service.domain.ReportJobFilterDomainService;
import ru.magnit.magreportbackend.service.domain.ScheduleCalendarDomainService;
import ru.magnit.magreportbackend.service.domain.ScheduleDomainService;
import ru.magnit.magreportbackend.service.domain.ScheduleTaskDomainService;
import ru.magnit.magreportbackend.service.domain.ScheduleTypeDomainService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final UserDomainService userDomainService;
    private final ScheduleCalendarDomainService calendarDomainService;
    private final ScheduleDomainService scheduleDomainService;
    private final ScheduleTaskDomainService scheduleTaskDomainService;
    private final ReportDomainService reportDomainService;
    private final ScheduleTypeDomainService scheduleTypeDomainService;
    private final ReportJobFilterDomainService reportJobFilterDomainService;
    private final DestinationDomainService destinationDomainService;
    private final ReportService reportService;

    public void addNewDateScheduleCalendar(ScheduleCalendarAddRequest request) {
        calendarDomainService.addNewDates(request.getCountDays());
    }

    public Long addSchedule(ScheduleAddRequest request) {
        final var currentUser = userDomainService.getCurrentUser();
        return scheduleDomainService.addSchedule(currentUser, request);
    }

    public ScheduleResponse editSchedule(ScheduleAddRequest request) {
        scheduleDomainService.editSchedule(request);
        return scheduleDomainService.getSchedule(request.getId());
    }

    public ScheduleResponse getSchedule(ScheduleRequest request) {
        return scheduleDomainService.getSchedule(request.getId());
    }

    public void deleteSchedule(ScheduleRequest request) {
        scheduleDomainService.deleteSchedule(request);
    }

    public Long addScheduleTask(ScheduleTaskAddRequest request) {
        final var currentUser = userDomainService.getCurrentUser();
        return scheduleTaskDomainService.addScheduleTask(request, currentUser);
    }

    public ScheduleTaskResponse getScheduleTask(ScheduleTaskRequest request) {
        var response = scheduleTaskDomainService.getScheduleTask(request.getId());
        response.setReport(reportService.getScheduleReport(new ScheduleReportRequest(response.getReport().getId(), response.getId())));
        response.setReportJobFilters(response.getReport().getLastParameters());
        response.getReport().setPath(reportDomainService.getPathReport(response.getReport().getId()));

        return response;
    }

    public void deleteScheduleTask(ScheduleTaskRequest request) {
        scheduleTaskDomainService.deleteScheduleTask(request);
    }

    public List<ScheduleTaskShortResponse> getAllScheduleTask() {
        return scheduleTaskDomainService.getAllScheduleTask();
    }

    public List<ScheduleResponse> getAllSchedules() {
        return scheduleDomainService.getAllSchedule();
    }

    public List<ScheduleTaskResponse> getTaskForDate(LocalDateTime currentDate) {

        return scheduleDomainService.getTaskForDate(currentDate);
    }

    public UUID setExpiredCodeTask(Long id) {
        return scheduleTaskDomainService.setExpiredCodeTask(id);
    }

    public String activationExpiredTask(UUID activationCode) {
        return scheduleTaskDomainService.activationExpiredTask(activationCode);
    }

    public void updateStatusScheduleTask(Long taskId, ScheduleTaskStatusEnum status) {
        scheduleTaskDomainService.setStatusScheduleTask(taskId, status);
    }

    public List<ScheduleTaskResponse> getTaskForStatus(ScheduleTaskStatusEnum status) {
        return scheduleTaskDomainService.getTaskForStatus(status);
    }

    public void refreshStatusesAfterRestart() {
        scheduleTaskDomainService.refreshStatusesAfterRestart();
    }

    public boolean checkDatesInScheduleCalendar() {
        return calendarDomainService.checkDates();
    }

    public LocalDate getNextDateSchedule(ScheduleShortResponse schedule) {
      return scheduleDomainService.getNextDateSchedule(schedule.getId(), schedule.getType().getId())
                .stream()
                 .filter(date -> date.isAfter(LocalDate.now()))
                 .sorted()
                 .findFirst().orElse(null);
    }

    public List<ScheduleTypeResponse> getScheduleTypes() {
        return scheduleTypeDomainService.getAll();
    }

    public String startManualTask(String code) {

        try {
            var id = scheduleTaskDomainService.findScheduleTaskByCode(code);
            scheduleTaskDomainService.setStatusScheduleTask(id, ScheduleTaskStatusEnum.RUNNING);
            return "Задание на расписании успешно поставлено в очередь выполнения";
        } catch (Exception ex) {
            return "Произошла ошибка при запуске задания по расписанию: " + ex.getMessage();
        }

    }

    public void startRunTask(ScheduleTaskRequest request) {
        scheduleTaskDomainService.setStatusScheduleTask(request.getId(), ScheduleTaskStatusEnum.RUNNING);
    }

    public ScheduleTaskResponse editScheduleTask(ScheduleTaskAddRequest request) {
        final var currentUser = userDomainService.getCurrentUser();

        destinationDomainService.deleteByScheduleTask(request.getId());
        reportJobFilterDomainService.deleteReportJobFilterByScheduleTaskId(request.getId());

        clearDuplicatedFields(request);

        scheduleTaskDomainService.editScheduleTask(request, currentUser);

        request.getReportJobFilter().forEach(filter ->
                reportJobFilterDomainService.addReportJobFilterByScheduleTaskId(filter, request.getId()));

        return scheduleTaskDomainService.getScheduleTask(request.getId());
    }

    public List<ScheduleTaskResponse> getTaskWithDeadlineExpires(Long countDays) {
        return scheduleTaskDomainService.getTaskWithDeadlineExpires(countDays);
    }

    public ScheduleTaskStatusEnum switchStatusScheduleTask(ScheduleTaskRequest request) {

        var task = scheduleTaskDomainService.getScheduleTask(request.getId()).getStatus();
        ScheduleTaskStatusEnum status;
        switch (task) {
            case SCHEDULED, FAILED, EXPIRED, CHANGED -> {
                scheduleTaskDomainService.setStatusScheduleTask(request.getId(), ScheduleTaskStatusEnum.INACTIVE);
                status = ScheduleTaskStatusEnum.INACTIVE;
            }
            case INACTIVE -> {
                scheduleTaskDomainService.setStatusScheduleTask(request.getId(), ScheduleTaskStatusEnum.SCHEDULED);
                status = ScheduleTaskStatusEnum.SCHEDULED;
            }
            case RUNNING, COMPLETE -> throw new InvalidParametersException("Error update status: task in running");
            default -> throw new InvalidParametersException("Error update status: unknown status " + task);
        }
        return status;
    }

    private void clearDuplicatedFields(ScheduleTaskAddRequest request) {

        var report = reportService.getScheduleReport(new ScheduleReportRequest(request.getReportId(), request.getId()));
        var validateCodeIds = report
                .getAllFilters()
                .stream()
                .flatMap(f -> f.fields().stream())
                .filter(f -> f.type().equals(FilterFieldTypeEnum.CODE_FIELD))
                .map(FilterReportFieldResponse::id)
                .toList();

        var filtersType = report.getAllFilters().stream()
                .collect(Collectors.toMap(FilterReportResponse::id, FilterReportResponse::type));

        request.getReportJobFilter()
                .stream()
                .filter(filter -> filtersType.containsKey(filter.getFilterId()))
                .filter(filter -> filtersType.get(filter.getFilterId()).equals(FilterTypeEnum.TOKEN_INPUT))
                .forEach(filter -> filter.getParameters()
                        .forEach(t -> t.setValues(
                                t.getValues()
                                        .stream()
                                        .filter(tupleValue -> validateCodeIds.contains(tupleValue.getFieldId()))
                                        .toList())));
    }
}
