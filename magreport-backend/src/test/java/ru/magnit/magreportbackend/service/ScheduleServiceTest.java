package ru.magnit.magreportbackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTaskStatusEnum;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTaskTypeEnum;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTypeEnum;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.request.reportjob.ReportJobFilterRequest;
import ru.magnit.magreportbackend.dto.request.schedule.DestinationEmailAddRequest;
import ru.magnit.magreportbackend.dto.request.schedule.DestinationRoleAddRequest;
import ru.magnit.magreportbackend.dto.request.schedule.DestinationUserAddRequest;
import ru.magnit.magreportbackend.dto.request.schedule.ScheduleAddRequest;
import ru.magnit.magreportbackend.dto.request.schedule.ScheduleCalendarAddRequest;
import ru.magnit.magreportbackend.dto.request.schedule.ScheduleRequest;
import ru.magnit.magreportbackend.dto.request.schedule.ScheduleTaskAddRequest;
import ru.magnit.magreportbackend.dto.request.schedule.ScheduleTaskRequest;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ExcelTemplateResponse;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterGroupResponse;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportFieldResponse;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportJobFilterResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportResponse;
import ru.magnit.magreportbackend.dto.response.report.ScheduleReportResponse;
import ru.magnit.magreportbackend.dto.response.schedule.DestinationEmailResponse;
import ru.magnit.magreportbackend.dto.response.schedule.DestinationRoleResponse;
import ru.magnit.magreportbackend.dto.response.schedule.DestinationUserResponse;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleResponse;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleShortResponse;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleTaskResponse;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleTaskShortResponse;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleTypeResponse;
import ru.magnit.magreportbackend.dto.tuple.Tuple;
import ru.magnit.magreportbackend.dto.tuple.TupleValue;
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
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    private static final Long ID = 1L;
    private static final String NAME = "Test folder";
    private static final String DESCRIPTION = "Folder description";
    private static final Long VALUE = 1L;
    private static final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    private static final String CODE = "CODE";
    private static final String REPORT_BODY_MAIL = "MESSAGE";
    private static final String REPORT_MESSAGE_TITLE = "TITLE";

    private static final String ERROR_BODY_MAIL = "ERROR MESSAGE";
    private static final String ERROR_MESSAGE_TITLE = "ERROR TITLE";
    private static final Long EXCEL_TEMPLATE = 1L;
    private static final Long REPORT = 2L;
    private static final Long TASK_TYPE = 1L;
    private static final Long RENEWAL_PERIOD = 180L;
    private static final Boolean SEND_EMPTY_REPORT = true;
    private static final Long STATUS_TASK = 0L;
    private static final UUID UUID_CODE = UUID.randomUUID();


    @Mock
    private ScheduleDomainService scheduleDomainService;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private ScheduleCalendarDomainService calendarDomainService;

    @Mock
    private ScheduleTaskDomainService scheduleTaskDomainService;

    @Mock
    private ReportDomainService reportDomainService;

    @Mock
    private ScheduleTypeDomainService scheduleTypeDomainService;

    @Mock
    private ReportJobFilterDomainService reportJobFilterDomainService;

    @Mock
    private DestinationDomainService destinationDomainService;

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ScheduleService service;


    @Test
    void addSchedule() {
        UserView user = new UserView();
        ScheduleAddRequest request = getScheduleAddRequest();

        when(userDomainService.getCurrentUser()).thenReturn(user);
        when(scheduleDomainService.addSchedule(any(), any())).thenReturn(ID);

        var result = service.addSchedule(request);

        assertEquals(ID, result);
        verify(userDomainService).getCurrentUser();
        verify(scheduleDomainService).addSchedule(any(), any());
        verifyNoMoreInteractions(scheduleDomainService, userDomainService);

    }

    @Test
    void addNewDateScheduleCalendar() {

        service.addNewDateScheduleCalendar(new ScheduleCalendarAddRequest(1L));

        verify(calendarDomainService).addNewDates(any());
        verifyNoMoreInteractions(calendarDomainService);
    }

    @Test
    void editSchedule() {
        when(scheduleDomainService.getSchedule(any())).thenReturn(getScheduleResponse());

        var response = service.editSchedule(getScheduleAddRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(ID, response.getType().getId());
        assertTrue(response.getTasks().isEmpty());
        assertEquals(VALUE, response.getSecond());
        assertEquals(VALUE, response.getMinute());
        assertEquals(VALUE, response.getHour());
        assertEquals(VALUE, response.getDay());
        assertEquals(VALUE, response.getDayWeek());
        assertEquals(VALUE, response.getMonth());
        assertEquals(VALUE, response.getYear());
        assertEquals(VALUE, response.getDayEndMonth());
        assertEquals(VALUE, response.getWeekMonth());
        assertEquals(VALUE, response.getWeekEndMonth());

        verify(scheduleDomainService).getSchedule(any());
        verify(scheduleDomainService).editSchedule(any());
        verifyNoMoreInteractions(scheduleDomainService);
    }

    @Test
    void getSchedule() {
        when(scheduleDomainService.getSchedule(any())).thenReturn(getScheduleResponse());

        var response = service.getSchedule(getScheduleRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(ID, response.getType().getId());
        assertTrue(response.getTasks().isEmpty());
        assertEquals(VALUE, response.getSecond());
        assertEquals(VALUE, response.getMinute());
        assertEquals(VALUE, response.getHour());
        assertEquals(VALUE, response.getDay());
        assertEquals(VALUE, response.getDayWeek());
        assertEquals(VALUE, response.getMonth());
        assertEquals(VALUE, response.getYear());
        assertEquals(VALUE, response.getDayEndMonth());
        assertEquals(VALUE, response.getWeekMonth());
        assertEquals(VALUE, response.getWeekEndMonth());

        verify(scheduleDomainService).getSchedule(any());
        verifyNoMoreInteractions(scheduleDomainService);

    }

    @Test
    void deleteSchedule() {

        service.deleteSchedule(getScheduleRequest());
        verify(scheduleDomainService).deleteSchedule(any());
        verifyNoMoreInteractions(scheduleDomainService);
    }

    @Test
    void addScheduleTask() {

        when(scheduleTaskDomainService.addScheduleTask(any(), any())).thenReturn(ID);

        var response = service.addScheduleTask(getScheduleTaskAddRequest());

        assertEquals(ID, response);

        verify(userDomainService).getCurrentUser();
        verify(scheduleTaskDomainService).addScheduleTask(any(), any());
        verifyNoMoreInteractions(userDomainService, scheduleTaskDomainService);
    }

    @Test
    void getScheduleTask() {
        when(scheduleTaskDomainService.getScheduleTask(any())).thenReturn(getScheduleTaskResponse());
        when(reportService.getScheduleReport(any())).thenReturn(new ReportResponse().setId(REPORT));
        when(reportDomainService.getPathReport(any()))
                .thenReturn(Collections.singletonList(new FolderNodeResponse(ID, ID, NAME, DESCRIPTION,CREATED_TIME,MODIFIED_TIME)));

        var response = service.getScheduleTask(getScheduleTaskRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(STATUS_TASK, response.getStatus().getId());
        assertEquals(REPORT_BODY_MAIL, response.getReportBodyMail());
        assertEquals(REPORT_MESSAGE_TITLE, response.getReportTitleMail());
        assertEquals(ERROR_BODY_MAIL, response.getErrorBodyMail());
        assertEquals(ERROR_MESSAGE_TITLE, response.getErrorTitleMail());
        assertEquals(EXCEL_TEMPLATE, response.getExcelTemplate().getId());
        assertEquals(REPORT, response.getReport().getId());
        assertEquals(TASK_TYPE, response.getTypeTask().getId());
        assertEquals(SEND_EMPTY_REPORT, response.getSendEmptyReport());
        assertEquals(RENEWAL_PERIOD, response.getRenewalPeriod());
        assertEquals(1, response.getDestinationEmails().size());
        assertEquals(1, response.getDestinationUsers().size());
        assertEquals(1, response.getDestinationRoles().size());
        assertEquals(1, response.getSchedules().size());


        verify(reportDomainService).getPathReport(any());
        verify(reportService).getScheduleReport(any());
        verify(scheduleTaskDomainService).getScheduleTask(any());
        verifyNoMoreInteractions(reportDomainService, reportService, scheduleTaskDomainService);

    }

    @Test
    void deleteScheduleTask() {

        service.deleteScheduleTask(getScheduleTaskRequest());

        verify(scheduleTaskDomainService).deleteScheduleTask(any());
        verifyNoMoreInteractions(scheduleTaskDomainService);

    }

    @Test
    void getAllScheduleTask() {

        when(scheduleTaskDomainService.getAllScheduleTask()).thenReturn(Collections.singletonList(getScheduleTaskShortResponse()));

        var responses = service.getAllScheduleTask();

        assertEquals(1, responses.size());

        verify(scheduleTaskDomainService).getAllScheduleTask();
        verifyNoMoreInteractions(scheduleTaskDomainService);
    }

    @Test
    void getAllSchedules() {

        when(scheduleDomainService.getAllSchedule()).thenReturn(Collections.singletonList(getScheduleResponse()));

        var responses = service.getAllSchedules();

        assertEquals(1, responses.size());

        verify(scheduleDomainService).getAllSchedule();
        verifyNoMoreInteractions(scheduleDomainService);

    }

    @Test
    void getTaskForDate() {

        when(scheduleDomainService.getTaskForDate(any())).thenReturn(Collections.singletonList(getScheduleTaskResponse()));

        var responses = service.getTaskForDate(CREATED_TIME);

        assertEquals(1, responses.size());

        verify(scheduleDomainService).getTaskForDate(any());
        verifyNoMoreInteractions(scheduleDomainService);
    }

    @Test
    void setExpiredCodeTask() {

        when(scheduleTaskDomainService.setExpiredCodeTask(any())).thenReturn(UUID_CODE);

        var response = service.setExpiredCodeTask(ID);

        assertEquals(UUID_CODE, response);

        verify(scheduleTaskDomainService).setExpiredCodeTask(any());
        verifyNoMoreInteractions(scheduleTaskDomainService);
    }

    @Test
    void activationExpiredTask() {
        when(scheduleTaskDomainService.activationExpiredTask(any())).thenReturn(REPORT_BODY_MAIL);

        var response = service.activationExpiredTask(UUID_CODE);

        assertEquals(REPORT_BODY_MAIL, response);

        verify(scheduleTaskDomainService).activationExpiredTask(any());
        verifyNoMoreInteractions(scheduleTaskDomainService);
    }

    @Test
    void updateStatusScheduleTask() {

        service.updateStatusScheduleTask(ID, ScheduleTaskStatusEnum.SCHEDULED);

        verify(scheduleTaskDomainService).setStatusScheduleTask(any(), any());
        verifyNoMoreInteractions(scheduleTaskDomainService);
    }

    @Test
    void getTaskForStatus() {
        when(scheduleTaskDomainService.getTaskForStatus(any())).thenReturn(Collections.singletonList(getScheduleTaskResponse()));

        var responses = service.getTaskForStatus(ScheduleTaskStatusEnum.getById(STATUS_TASK));

        assertEquals(1, responses.size());

        verify(scheduleTaskDomainService).getTaskForStatus(any());
        verifyNoMoreInteractions(scheduleTaskDomainService);
    }

    @Test
    void refreshStatusesAfterRestart() {

        service.refreshStatusesAfterRestart();

        verify(scheduleTaskDomainService).refreshStatusesAfterRestart();
        verifyNoMoreInteractions(scheduleTaskDomainService);
    }

    @Test
    void checkDatesInScheduleCalendar() {
        when(calendarDomainService.checkDates()).thenReturn(true);

        var response = service.checkDatesInScheduleCalendar();

        assertTrue(response);

        verify(calendarDomainService).checkDates();
        verifyNoMoreInteractions(calendarDomainService);

    }

    @Test
    void getNextDateSchedule() {

        when(scheduleDomainService.getNextDateSchedule(any(), any())).thenReturn(Collections.singletonList(LocalDate.now().plusDays(3)));

        var response = service.getNextDateSchedule(new ScheduleShortResponse().setId(ID).setType(ScheduleTypeEnum.DAY_END_MONTH));

        assertEquals(LocalDate.now().plusDays(3), response);

        verify(scheduleDomainService).getNextDateSchedule(any(), any());
        verifyNoMoreInteractions(scheduleDomainService);
    }

    @Test
    void getScheduleTypes() {

        when(scheduleTypeDomainService.getAll()).thenReturn(Collections.singletonList(new ScheduleTypeResponse()));

        var responses = service.getScheduleTypes();

        assertEquals(1, responses.size());

        verify(scheduleTypeDomainService).getAll();
        verifyNoMoreInteractions(scheduleTypeDomainService);
    }

    @Test
    void startManualTask() {

        when(scheduleTaskDomainService.findScheduleTaskByCode(any())).thenReturn(ID);
        service.startManualTask(CODE);
        verify(scheduleTaskDomainService).setStatusScheduleTask(any(), any());
        verify(scheduleTaskDomainService).findScheduleTaskByCode(any());
        verifyNoMoreInteractions(scheduleTaskDomainService);

        Mockito.reset(scheduleTaskDomainService);

        when(scheduleTaskDomainService.findScheduleTaskByCode(CODE)).thenThrow(new InvalidParametersException(""));
        service.startManualTask(CODE);
        verify(scheduleTaskDomainService).findScheduleTaskByCode(any());
        verifyNoMoreInteractions(scheduleTaskDomainService);
    }

    @Test
    void startRunTask() {

        service.startRunTask(getScheduleTaskRequest());

        verify(scheduleTaskDomainService).setStatusScheduleTask(any(), any());
        verifyNoMoreInteractions(scheduleTaskDomainService);
    }

    @Test
    void editScheduleTask() {

        when(reportService.getScheduleReport(any())).thenReturn(getReportResponse());
        when(scheduleTaskDomainService.getScheduleTask(any())).thenReturn(getScheduleTaskResponse());

        var response = service.editScheduleTask(getScheduleTaskAddRequest());

        assertNotNull(response);

        verify(userDomainService).getCurrentUser();
        verify(destinationDomainService).deleteByScheduleTask(any());
        verify(reportJobFilterDomainService).deleteReportJobFilterByScheduleTaskId(any());
        verify(scheduleTaskDomainService).editScheduleTask(any(), any());
        verify(reportJobFilterDomainService).addReportJobFilterByScheduleTaskId(any(), any());
        verify(scheduleTaskDomainService).getScheduleTask(any());
        verify(reportService).getScheduleReport(any());
        verifyNoMoreInteractions(userDomainService, destinationDomainService, reportJobFilterDomainService, reportService);

    }

    @Test
    void getTaskWithDeadlineExpires() {

        when(scheduleTaskDomainService.getTaskWithDeadlineExpires(any())).thenReturn(Collections.singletonList(getScheduleTaskResponse()));

        var response = service.getTaskWithDeadlineExpires(RENEWAL_PERIOD);

        assertEquals(1, response.size());

        verify(scheduleTaskDomainService).getTaskWithDeadlineExpires(any());
        verifyNoMoreInteractions(scheduleTaskDomainService);
    }

    @Test
    void switchStatusScheduleTask() {

        when(scheduleTaskDomainService.getScheduleTask(any())).thenReturn(getScheduleTaskResponse());
        var response = service.switchStatusScheduleTask(getScheduleTaskRequest());
        assertEquals(ScheduleTaskStatusEnum.INACTIVE, response);


        when(scheduleTaskDomainService.getScheduleTask(any())).thenReturn(getScheduleTaskResponse().setStatus(ScheduleTaskStatusEnum.INACTIVE));
        response = service.switchStatusScheduleTask(getScheduleTaskRequest());
        assertEquals(ScheduleTaskStatusEnum.SCHEDULED, response);


        when(scheduleTaskDomainService.getScheduleTask(any())).thenReturn(getScheduleTaskResponse().setStatus(ScheduleTaskStatusEnum.RUNNING));
        var request = getScheduleTaskRequest();
        assertThrows(InvalidParametersException.class, () -> service.switchStatusScheduleTask(request));

        verify(scheduleTaskDomainService, times(3)).getScheduleTask(any());
        verify(scheduleTaskDomainService, times(2)).setStatusScheduleTask(any(), any());
        verifyNoMoreInteractions(scheduleTaskDomainService);

    }


    private ScheduleAddRequest getScheduleAddRequest() {
        return new ScheduleAddRequest()
                .setId(ID);
    }

    private ScheduleRequest getScheduleRequest() {
        return new ScheduleRequest()
                .setId(ID);
    }


    private ScheduleResponse getScheduleResponse() {
        return new ScheduleResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setType(ScheduleTypeEnum.getById(ID))
                .setTasks(Collections.emptyList())
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME)
                .setSecond(VALUE)
                .setMinute(VALUE)
                .setHour(VALUE)
                .setDay(VALUE)
                .setDayWeek(VALUE)
                .setMonth(VALUE)
                .setYear(VALUE)
                .setDayEndMonth(VALUE)
                .setWeekEndMonth(VALUE)
                .setWeekMonth(VALUE);

    }

    private ScheduleTaskAddRequest getScheduleTaskAddRequest() {
        return new ScheduleTaskAddRequest()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCode(CODE)
                .setReportBodyMail(REPORT_BODY_MAIL)
                .setReportTitleMail(REPORT_MESSAGE_TITLE)
                .setErrorBodyMail(ERROR_BODY_MAIL)
                .setErrorTitleMail(ERROR_MESSAGE_TITLE)
                .setExcelTemplateId(EXCEL_TEMPLATE)
                .setReportId(REPORT)
                .setTaskTypeId(TASK_TYPE)
                .setRenewalPeriod(RENEWAL_PERIOD)
                .setSendEmptyReport(SEND_EMPTY_REPORT)
                .setDestinationEmails(Collections.singletonList(new DestinationEmailAddRequest()))
                .setDestinationUsers(Collections.singletonList(new DestinationUserAddRequest()))
                .setDestinationRoles(Collections.singletonList(new DestinationRoleAddRequest()))
                .setSchedules(Collections.singletonList(getScheduleRequest()))
                .setReportJobFilter(Collections.singletonList(new ReportJobFilterRequest()
                                .setFilterId(ID)
                                .setParameters(Collections.singletonList(new Tuple().setValues(Collections.singletonList(new TupleValue(ID, null)))))

                        )

                );

    }

    private ScheduleTaskRequest getScheduleTaskRequest() {
        return new ScheduleTaskRequest()
                .setId(ID);
    }

    private ScheduleTaskResponse getScheduleTaskResponse() {
        return new ScheduleTaskResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setStatus(ScheduleTaskStatusEnum.getById(STATUS_TASK))
                .setReportBodyMail(REPORT_BODY_MAIL)
                .setReportTitleMail(REPORT_MESSAGE_TITLE)
                .setErrorBodyMail(ERROR_BODY_MAIL)
                .setErrorTitleMail(ERROR_MESSAGE_TITLE)
                .setExcelTemplate(new ExcelTemplateResponse().setId(EXCEL_TEMPLATE))
                .setReport(new ReportResponse().setId(REPORT))
                .setTypeTask(ScheduleTaskTypeEnum.getById(TASK_TYPE))
                .setRenewalPeriod(RENEWAL_PERIOD)
                .setSendEmptyReport(SEND_EMPTY_REPORT)
                .setDestinationEmails(Collections.singletonList(new DestinationEmailResponse()))
                .setDestinationUsers(Collections.singletonList(new DestinationUserResponse()))
                .setDestinationRoles(Collections.singletonList(new DestinationRoleResponse()))
                .setSchedules(Collections.singletonList(new ScheduleShortResponse()))
                .setReportJobFilters(Collections.singletonList(new ReportJobFilterResponse()));


    }

    private ScheduleTaskShortResponse getScheduleTaskShortResponse() {
        return new ScheduleTaskShortResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setStatus(ScheduleTaskStatusEnum.getById(STATUS_TASK))
                .setReportBodyMail(REPORT_BODY_MAIL)
                .setReportTitleMail(REPORT_MESSAGE_TITLE)
                .setErrorBodyMail(ERROR_BODY_MAIL)
                .setErrorTitleMail(ERROR_MESSAGE_TITLE)
                .setReport(new ScheduleReportResponse().setId(REPORT))
                .setTypeTask(ScheduleTaskTypeEnum.getById(TASK_TYPE))
                .setRenewalPeriod(RENEWAL_PERIOD)
                .setSendEmptyReport(SEND_EMPTY_REPORT)
                .setDestinationEmails(Collections.singletonList(new DestinationEmailResponse()))
                .setDestinationUsers(Collections.singletonList(new DestinationUserResponse()))
                .setDestinationRoles(Collections.singletonList(new DestinationRoleResponse()))
                .setSchedules(Collections.singletonList(new ScheduleShortResponse()));


    }

    private ReportResponse getReportResponse() {
        return new ReportResponse()
                .setFilterGroup(
                        new FilterGroupResponse(
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                Collections.singletonList(getFilterReportResponse()),
                                null,
                                null
                        )
                );
    }

    private FilterReportResponse getFilterReportResponse() {
        return new FilterReportResponse(
                ID,
                null,
                FilterTypeEnum.TOKEN_INPUT,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                Collections.singletonList(getFilterReportFieldResponse()),
                null,
                null,
                null
        );
    }

    private FilterReportFieldResponse getFilterReportFieldResponse() {
        return new FilterReportFieldResponse(
                ID,
                null,
                null,
                FilterFieldTypeEnum.CODE_FIELD,
                null,
                null, null, null, null, null, null);
    }

}