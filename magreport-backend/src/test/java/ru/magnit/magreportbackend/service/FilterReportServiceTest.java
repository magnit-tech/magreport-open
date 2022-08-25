package ru.magnit.magreportbackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.filterreport.GroupOperationTypeEnum;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTaskStatusEnum;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.request.filterinstance.ChildNodesRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.ListValuesCheckRequest;
import ru.magnit.magreportbackend.dto.request.filterreport.FilterAddRequest;
import ru.magnit.magreportbackend.dto.request.filterreport.FilterGroupAddRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportRequest;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterNodeResponse;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterGroupResponse;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportChildNodesResponse;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportResponse;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportValuesCheckResponse;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportValuesResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportResponse;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleTaskResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.service.domain.DataSetDomainService;
import ru.magnit.magreportbackend.service.domain.FilterInstanceDomainService;
import ru.magnit.magreportbackend.service.domain.FilterReportDomainService;
import ru.magnit.magreportbackend.service.domain.MailTextDomainService;
import ru.magnit.magreportbackend.service.domain.ReportDomainService;
import ru.magnit.magreportbackend.service.domain.ScheduleTaskDomainService;
import ru.magnit.magreportbackend.service.domain.SecurityFilterDomainService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilterReportServiceTest {

    private static final long ID = 1L;
    private static final long REPORT_ID = 2L;
    private static final String NAME = "Test filter group";
    private static final String CODE = "Test code";
    private static final String DESCRIPTION = "Test filter group description";
    private static final LocalDateTime CREATED = LocalDateTime.now();
    private static final LocalDateTime MODIFIED = CREATED.plusMinutes(2);
    private static final long ORDINAL = 1L;
    private static final long DATASET_ID = 1L;

    @InjectMocks
    private FilterReportService service;

    @Mock
    private FilterReportDomainService domainService;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private FilterInstanceDomainService filterInstanceDomainService;

    @Mock
    private SecurityFilterDomainService securityFilterDomainService;

    @Mock
    private ReportDomainService reportDomainService;

    @Mock
    private DataSetDomainService dataSetDomainService;

    @Mock
    private ScheduleTaskDomainService scheduleTaskDomainService;

    @Mock
    private MailTextDomainService mailTextDomainService;


    @Test
    void getFilters() {
        given(domainService.getFilters(REPORT_ID)).willReturn(getFilterReportResponse());

        var result = service.getFilters(getRequest());

        assertEquals(ID, result.id());
        assertEquals(REPORT_ID, result.reportId());
        assertEquals(NAME, result.name());
        assertEquals(DESCRIPTION, result.description());
        assertEquals(false, result.linkedFilters());
        assertEquals(GroupOperationTypeEnum.AND, result.operationType());
        assertEquals(CREATED, result.created());
        assertEquals(MODIFIED, result.modified());

        verify(domainService).getFilters(anyLong());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void addFiltersException() {

        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setId(ID));
        when(filterInstanceDomainService.addMissingFields(any()))
                .thenReturn(getSource().setFilters(Collections.singletonList(new FilterAddRequest().setCode("1"))));
        when(dataSetDomainService.getDataSet(any())).thenReturn(new DataSetResponse().setTypeId(1L));
        when(reportDomainService.getReport(any())).thenReturn(new ReportResponse().setDataSetId(0L));

        var request = getSource();
        assertThrows(InvalidParametersException.class, () -> service.addFilters(request));

        when(filterInstanceDomainService.addMissingFields(any()))
                .thenReturn(getSource().setFilters(Collections.singletonList(new FilterAddRequest().setCode(""))));

        assertThrows(InvalidParametersException.class, () -> service.addFilters(request));

        verify(userDomainService, times(2)).getCurrentUser();
        verify(reportDomainService, times(2)).getReport(any());
        verify(dataSetDomainService, times(2)).getDataSet(any());
        verify(filterInstanceDomainService, times(2)).addMissingFields(any());

        verifyNoMoreInteractions(userDomainService,reportDomainService,dataSetDomainService,filterInstanceDomainService);
    }

    @Test
    void addFilters() {

        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setId(ID));
        when(filterInstanceDomainService.addMissingFields(any())).thenReturn(getSource());
        when(domainService.getFilters(any())).thenReturn(getFilterReportResponse());
        when(reportDomainService.getReport(any())).thenReturn(new ReportResponse().setDataSetId(0L));
        when(dataSetDomainService.getDataSet(any())).thenReturn(new DataSetResponse().setTypeId(0L));
        when(scheduleTaskDomainService.findScheduleTaskByReport(anyLong())).thenReturn(Collections.singletonList(getScheduleTaskResponse()));

        when(dataSetDomainService.getDataSet(any())).thenReturn(new DataSetResponse().setTypeId(1L));

       var response = service.addFilters(getSource());

        assertEquals(ID, response.id());
        assertEquals(REPORT_ID, response.reportId());
        assertEquals(NAME, response.name());
        assertEquals(DESCRIPTION, response.description());
        assertEquals(false, response.linkedFilters());
        assertEquals(GroupOperationTypeEnum.AND, response.operationType());
        assertEquals(CREATED, response.created());
        assertEquals(MODIFIED, response.modified());


        verify(userDomainService).getCurrentUser();
        verify(filterInstanceDomainService).addMissingFields(any());
        verify(domainService ).getFilters(any());
        verify(domainService).addFilters(any(), any());
        verify(reportDomainService).getReport(any());
        verify(dataSetDomainService).getDataSet(any());
        verify(scheduleTaskDomainService).setStatusScheduleTask(any(), any());
        verify(mailTextDomainService).sendScheduleMailChanged(any());

        verifyNoMoreInteractions(userDomainService, filterInstanceDomainService, domainService, reportDomainService, dataSetDomainService);

    }

    @Test
    void getFilterReportValues() {
        when(domainService.getFilterReportValues(any())).thenReturn(getFilterReportValueResponse());

        assertNotNull(service.getFilterReportValues(any()));

        verify(domainService).getFilterReportValues(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void FilterReportChildNodesResponse() {

        when(userDomainService.getCurrentUserRoles(any())).thenReturn(Collections.singletonList(new RoleView().setId(ID)));
        when(domainService.getFilterReportDataSetId(anyLong())).thenReturn(DATASET_ID);
        when(securityFilterDomainService.getEffectiveSettings(any(), any())).thenReturn(Collections.emptyList());
        when(domainService.getChildNodes(any(), any())).thenReturn(getFilterReportChildNodesResponse());

        assertNotNull(service.getChildNodes(new ChildNodesRequest().setFilterId(ID)));

        verify(userDomainService).getCurrentUserRoles(any());
        verify(domainService).getFilterReportDataSetId(anyLong());
        verify(securityFilterDomainService).getEffectiveSettings(any(), any());
        verify(domainService).getChildNodes(any(), any());

        verifyNoMoreInteractions(userDomainService);
        verifyNoMoreInteractions(domainService);
        verifyNoMoreInteractions(securityFilterDomainService);

    }

    @Test
    void checkFilterReportValues() {

        when(domainService.checkFilterReportValues(any())).thenReturn(new FilterReportValuesCheckResponse(new FilterReportResponse(), Collections.emptyList()));

        var response = service.checkFilterReportValues(new ListValuesCheckRequest());
        assertNotNull(response);

        verify(domainService).checkFilterReportValues(any());
        verifyNoMoreInteractions(domainService);
    }

    private ReportRequest getRequest() {
        return new ReportRequest().setId(REPORT_ID);
    }

    private FilterGroupAddRequest getSource() {
        return new FilterGroupAddRequest()
                .setReportId(REPORT_ID)
                .setChildGroups(
                        Arrays.asList(
                                new FilterGroupAddRequest()
                                        .setFilters(Collections.singletonList(
                                                new FilterAddRequest().setCode("1")
                                        )),

                                new FilterGroupAddRequest()
                                        .setFilters(Collections.singletonList(
                                                new FilterAddRequest().setCode("2")
                                        ))
                        )
                );
    }

    private FilterGroupResponse getFilterReportResponse() {

        return new FilterGroupResponse(
                ID,
                REPORT_ID,
                CODE,
                NAME,
                DESCRIPTION,
                ORDINAL,
                false,
                false,
                GroupOperationTypeEnum.AND,
                Collections.emptyList(),
                Collections.emptyList(),
                CREATED,
                MODIFIED);
    }

    private FilterReportValuesResponse getFilterReportValueResponse() {

        return new FilterReportValuesResponse(new FilterReportResponse(), Collections.emptyList());
    }

    private FilterReportChildNodesResponse getFilterReportChildNodesResponse() {

        return new FilterReportChildNodesResponse(
                new FilterReportResponse(),
                ID,
                new FilterNodeResponse(ID, ID, "testNodeID", "testNameNode", Collections.emptyList()));
    }

    private ScheduleTaskResponse getScheduleTaskResponse() {
        return new ScheduleTaskResponse()
                .setId(ID)
                .setStatus(ScheduleTaskStatusEnum.SCHEDULED);

    }

}