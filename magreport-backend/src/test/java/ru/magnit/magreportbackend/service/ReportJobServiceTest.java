package ru.magnit.magreportbackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum;
import ru.magnit.magreportbackend.domain.filterreport.GroupOperationTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobStateEnum;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobStatusEnum;
import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportFilterGroupData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobData;
import ru.magnit.magreportbackend.dto.request.reportjob.ExcelReportRequest;
import ru.magnit.magreportbackend.dto.request.reportjob.ReportJobAddRequest;
import ru.magnit.magreportbackend.dto.request.reportjob.ReportJobFilterRequest;
import ru.magnit.magreportbackend.dto.request.reportjob.ReportJobRequest;
import ru.magnit.magreportbackend.dto.request.reportjob.ReportPageRequest;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterGroupResponse;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderRoleResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportShortResponse;
import ru.magnit.magreportbackend.dto.response.reportjob.ReportJobResponse;
import ru.magnit.magreportbackend.dto.response.reportjob.ReportPageResponse;
import ru.magnit.magreportbackend.dto.response.reportjob.ReportSqlQueryResponse;
import ru.magnit.magreportbackend.dto.response.user.UserShortResponse;
import ru.magnit.magreportbackend.dto.tuple.Tuple;
import ru.magnit.magreportbackend.dto.tuple.TupleValue;
import ru.magnit.magreportbackend.exception.PermissionDeniedException;
import ru.magnit.magreportbackend.service.domain.AvroReportDomainService;
import ru.magnit.magreportbackend.service.domain.ExcelReportDomainService;
import ru.magnit.magreportbackend.service.domain.ExcelTemplateDomainService;
import ru.magnit.magreportbackend.service.domain.FilterReportDomainService;
import ru.magnit.magreportbackend.service.domain.FolderDomainService;
import ru.magnit.magreportbackend.service.domain.FolderPermissionsDomainService;
import ru.magnit.magreportbackend.service.domain.JobDomainService;
import ru.magnit.magreportbackend.service.domain.ReportDomainService;
import ru.magnit.magreportbackend.service.domain.TokenService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportJobServiceTest {

    private static final Long ID = 1L;
    private static final Long REPORT_ID = 2L;
    private static final Long JOB_ID = 5L;
    private static final Long FILTER_ID = 10L;

    @InjectMocks
    private ReportJobService service;

    @Mock
    private JobDomainService jobDomainService;

    @Mock
    private ExcelReportDomainService excelReportDomainService;

    @Mock
    private AvroReportDomainService avroReportDomainService;

    @Mock
    private ReportDomainService reportDomainService;

    @Mock
    private FilterReportDomainService filterReportDomainService;

    @Mock
    private ExcelTemplateDomainService excelTemplateDomainService;

    @Mock
    private FolderDomainService folderDomainService;

    @Mock
    private FolderPermissionsDomainService folderPermissionsDomainService;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private TokenService tokenService;

    @Test
    void getExcelReport() {

        //job not complete
        ExcelReportRequest request = spy(getExcelReportRequest());
        ReportJobData jobData = getReportJobData(false);

        when(jobDomainService.getJobData(ID)).thenReturn(jobData);
        when(excelTemplateDomainService.getTemplatePathForReport(ID, 1L)).thenReturn("");

        var result = service.getExcelReport(1L, 1L);

        assertNull(result);
        verify(jobDomainService).getJobData(ID);

        verify(excelReportDomainService).getExcelReport(any(), anyString(), anyLong());
        verify(excelReportDomainService).createExcelReport(any(), any(), anyLong());
        verifyNoMoreInteractions(jobDomainService, excelReportDomainService, avroReportDomainService, reportDomainService, filterReportDomainService);


        Mockito.reset(request, jobDomainService, excelReportDomainService);

        //job complete
        jobData = getReportJobData(true);

        when(jobDomainService.getJobData(ID)).thenReturn(jobData);
        when(excelReportDomainService.getExcelReport(any(), anyString(), anyLong())).thenReturn(mock(StreamingResponseBody.class));

        result = service.getExcelReport(1L,1L);

        assertNotNull(result);

        verify(jobDomainService).getJobData(ID);
        verify(excelReportDomainService).getExcelReport(any(), anyString(), anyLong());
        verify(excelReportDomainService).createExcelReport(any(), any(), anyLong());
        verifyNoMoreInteractions(jobDomainService, excelReportDomainService, avroReportDomainService, reportDomainService, filterReportDomainService);

    }

    @Test
    void addJob() {

        // no job filters
        ReportResponse reportResponse = mock(ReportResponse.class);
        ReportJobAddRequest request = spy(getReportJobAddRequest());
        final var permissionsResponse = new FolderRoleResponse(1L, FolderAuthorityEnum.WRITE);

        when(reportResponse.getAllFilters()).thenReturn(Collections.emptyList());
        when(reportDomainService.getReport(anyLong())).thenReturn(reportResponse);
        when(jobDomainService.addJob(any())).thenReturn(ID);
        when(jobDomainService.getJob(anyLong())).thenReturn(getReportJobResponse());
        when(folderDomainService.getReportsFolders(anyLong())).thenReturn(Collections.singletonList(1L));
        when(folderPermissionsDomainService.getFoldersReportPermissionsForRoles(any(), any())).thenReturn(Collections.singletonList(permissionsResponse));
        when(userDomainService.getCurrentUserRoles(any())).thenReturn(Collections.emptyList());

        final var result = service.addJob(request);

        assertNotNull(result);

        verify(reportDomainService).getReport(anyLong());
        verify(jobDomainService).addJob(any());
        verify(jobDomainService).getJob(anyLong());
    }

    @Test
    void addJobNoMatchedRequestParameters() {
        // one job filter, not matchedRequestParameters
        ReportResponse reportResponse = mock(ReportResponse.class);
        FilterReportResponse filter = getFilterReportResponse(true);
        ReportJobAddRequest request = spy(getReportJobAddRequest());
        final var permissionsResponse = new FolderRoleResponse(1L, FolderAuthorityEnum.WRITE);

        when(reportDomainService.getReport(anyLong())).thenReturn(reportResponse);
        when(folderDomainService.getReportsFolders(anyLong())).thenReturn(Collections.emptyList());
        when(userDomainService.getCurrentUserRoles(any())).thenReturn(Collections.emptyList());

        assertThrows(PermissionDeniedException.class, () -> service.addJob(request));

        verify(request).getReportId();
        verify(reportDomainService).getReport(anyLong());

        verifyNoMoreInteractions(request, jobDomainService, excelReportDomainService, avroReportDomainService, reportDomainService, filterReportDomainService);

    }

    @Test
    void addJobEmptyValues() {
        // one job filter, has matched request parameters, empty cleaned value list

        ReportJobAddRequest request = getReportJobAddRequest();
        ReportResponse reportResponse = mock(ReportResponse.class);
        final var permissionsResponse = new FolderRoleResponse(1L, FolderAuthorityEnum.WRITE);

        when(reportDomainService.getReport(any())).thenReturn(reportResponse);
        when(folderDomainService.getReportsFolders(anyLong())).thenReturn(Collections.emptyList());
        when(userDomainService.getCurrentUserRoles(any())).thenReturn(Collections.emptyList());

        assertThrows(PermissionDeniedException.class, () -> service.addJob(request));

        verify(reportDomainService).getReport(any());
        verifyNoMoreInteractions(reportDomainService, filterReportDomainService, jobDomainService);
    }

    private FilterReportResponse getFilterReportResponse(boolean mandatory) {
        return new FilterReportResponse(FILTER_ID,
                1L,
                FilterTypeEnum.VALUE_LIST,
                false,
                mandatory,
                true,
                "name",
                "code",
                "desc",
                1,
                Collections.emptyList(),
                "user",
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    @Test
    void getJob() {

        when(jobDomainService.getJob(anyLong())).thenReturn(getReportJobResponse());

        var response = service.getJob(ID);
        assertNotNull(response);

        verify(jobDomainService).getJob(anyLong());
        verifyNoMoreInteractions(jobDomainService);

        Mockito.reset(jobDomainService);

        when(jobDomainService.getJob(anyLong())).thenReturn(getReportJobResponse());

        assertNotNull(service.getJob(getReportJobRequest()));

        verify(jobDomainService).getJob(anyLong());
        verifyNoMoreInteractions(jobDomainService);
    }


    @Test
    void getMyJobs() {

        when(jobDomainService.getMyJobs()).thenReturn(Collections.emptyList());

        final var result = service.getMyJobs();

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(jobDomainService).getMyJobs();

        verifyNoMoreInteractions(jobDomainService, excelReportDomainService, avroReportDomainService, reportDomainService, filterReportDomainService);

    }

    @Test
    void getAllJobs() {

        when(jobDomainService.getAllJobs()).thenReturn(Collections.emptyList());

        final var result = service.getAllJobs();

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(jobDomainService).getAllJobs();

        verifyNoMoreInteractions(jobDomainService, excelReportDomainService, avroReportDomainService, reportDomainService, filterReportDomainService);

    }

    @Test
    void getReportPage() {

        //job not complete
        ReportPageRequest request = spy(getReportPageRequest());
        ReportJobData jobData = getReportJobData(false);

        when(jobDomainService.getJobData(anyLong())).thenReturn(jobData);

        var result = service.getReportPage(request);

        assertNull(result);

        verify(request).getJobId();
        verify(jobDomainService).getJobData(anyLong());

        verifyNoMoreInteractions(request, jobDomainService, excelReportDomainService, avroReportDomainService, reportDomainService, filterReportDomainService);


        Mockito.reset(request, jobDomainService, avroReportDomainService);

        //job complete
        jobData = getReportJobData(true);

        when(jobDomainService.getJobData(anyLong())).thenReturn(jobData);
        when(avroReportDomainService.getPage(any(), anyLong(), anyLong())).thenReturn(getReportPageResponse());

        result = service.getReportPage(request);

        assertNotNull(result);

        verify(request).getJobId();
        verify(request).getPageNumber();
        verify(request).getRowsPerPage();
        verify(jobDomainService).getJobData(anyLong());
        verify(avroReportDomainService).getPage(any(), anyLong(), anyLong());

        verifyNoMoreInteractions(request, jobDomainService, excelReportDomainService, avroReportDomainService, reportDomainService, filterReportDomainService);

    }

    @Test
    void cancelJob() {

        ReportJobRequest request = spy(getReportJobRequest());

        when(jobDomainService.getJob(anyLong())).thenReturn(getReportJobResponse());

        final var result = service.cancelJob(request);

        assertNotNull(result);

        verify(request, times(2)).getJobId();
        verify(jobDomainService).cancelJob(anyLong());
        verify(jobDomainService).getJob(anyLong());

        verifyNoMoreInteractions(request, jobDomainService, excelReportDomainService, avroReportDomainService, reportDomainService, filterReportDomainService);
    }

    @Test
    void createExcelReport() {

        when(jobDomainService.getJobData(any())).thenReturn(getReportJobData(false));
        when(excelTemplateDomainService.getTemplatePathForReport(anyLong(), anyLong())).thenReturn("path");
        when(tokenService.getToken(any(),any())).thenReturn("token");

        var response = service.createExcelReport(new ExcelReportRequest().setId(ID).setExcelTemplateId(ID));
        assertNotNull(response);

        verify(jobDomainService).getJobData(any());
        verify(excelTemplateDomainService).getTemplatePathForReport(anyLong(), anyLong());
        verify(tokenService).getToken(any(),any());

        verifyNoMoreInteractions(jobDomainService, excelTemplateDomainService, tokenService);
    }

    @Test
    void getReportSize() {

        when(jobDomainService.getJobData(any())).thenReturn(getReportJobData(false));
        when(excelReportDomainService.getReportSize(anyLong(),anyLong())).thenReturn(1000L);

        var response = service.getReportSize(ID, ID);
        assertEquals(1000L, response);

        verify(jobDomainService).getJobData(any());
        verify(excelReportDomainService).getReportSize(anyLong(), anyLong());
        verifyNoMoreInteractions(jobDomainService, excelReportDomainService);
    }

    @Test
    void saveExcelReport() {

        when(jobDomainService.getJobData(any())).thenReturn(getReportJobData(false));
        when(excelTemplateDomainService.getTemplatePathForReport(anyLong(), anyLong())).thenReturn("path");

        service.saveExcelReport(getExcelReportRequest().setExcelTemplateId(ID));

        verify(jobDomainService).getJobData(any());
        verify(excelTemplateDomainService).getTemplatePathForReport(anyLong(), anyLong());
        verify(excelReportDomainService).saveReportToExcel(any(), any(), anyLong());
        verify(excelReportDomainService).moveReportToRms(anyLong(),anyLong());
        verifyNoMoreInteractions(jobDomainService, excelTemplateDomainService, excelReportDomainService);
    }

    @Test
    void deleteJob() {

        service.deleteJob(getReportJobRequest());

        verify(jobDomainService).deleteJob(anyLong());
        verifyNoMoreInteractions(jobDomainService);
    }

    @Test
    void deleteAllJobs() {

        service.deleteAllJobs();

        verify(jobDomainService).deleteAllJobs();
        verifyNoMoreInteractions(jobDomainService);
    }

    @Test
    void getSqlQuery() {

        when(jobDomainService.getSqlQuery(anyLong())).thenReturn(new ReportSqlQueryResponse(ID, "SqlQuery"));

        var response = service.getSqlQuery(getReportJobRequest());
        assertNotNull(response);

        verify(jobDomainService).getSqlQuery(anyLong());
        verifyNoMoreInteractions(jobDomainService);
    }

    @Test
    void checkDateParameters() {

      assertDoesNotThrow( ()-> ReflectionTestUtils.invokeMethod(service, "checkDateParameters", getReportJobAddRequest(), getReportResponse()));
    }


    private ReportJobData getReportJobData(boolean isComplete) {
        return new ReportJobData(1L,
                2L,
                3L,
                5L,
                4L,
                "User",
                1L,
                2L,
                3L,
                isComplete,
                new DataSourceData(1L, DataSourceTypeEnum.TERADATA, "url", "user", "pwd", Short.valueOf("1")),
                new ReportData(1L, "name", "desc", "schema", "table", Collections.emptyList(), new ReportFilterGroupData(1L, 1L, "test code", "test code", GroupOperationTypeEnum.AND, Collections.emptyList(), Collections.emptyList())),
                Collections.emptyList(),
                Collections.emptyList());
    }

    private ExcelReportRequest getExcelReportRequest() {
        return new ExcelReportRequest()
                .setId(ID);
    }

    private ReportJobRequest getReportJobRequest() {
        return new ReportJobRequest()
                .setJobId(JOB_ID);
    }

    private ReportJobResponse getReportJobResponse() {
        return new ReportJobResponse(1L,
                new ReportShortResponse(1L, "Report"),
                new UserShortResponse(1L, "User"),
                ReportJobStatusEnum.RUNNING,
                ReportJobStateEnum.NORMAL,
                "msg",
                100L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                Collections.emptyList(),
                true);
    }

    private ReportJobAddRequest getReportJobAddRequest() {
        final var parameters = new LinkedList<ReportJobFilterRequest>();
        parameters.add(
                new ReportJobFilterRequest()
                        .setFilterId(FILTER_ID)
                        .setOperationType(FilterOperationTypeEnum.IS_EQUAL)
                        .setParameters(Collections.singletonList(new Tuple().setValues(Collections.singletonList(new TupleValue().setValue("123"))))));
        return new ReportJobAddRequest()
                .setReportId(REPORT_ID)
                .setParameters(parameters);
    }

    private ReportPageResponse getReportPageResponse() {
        return new ReportPageResponse(
                1L,
                JOB_ID,
                1L,
                100L,
                Collections.emptyList()
        );
    }

    private ReportPageRequest getReportPageRequest() {
        return new ReportPageRequest()
                .setJobId(JOB_ID)
                .setPageNumber(1L)
                .setRowsPerPage(100L);
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
                                Collections.singletonList(new FilterReportResponse(
                                        2L,
                                        null,
                                        FilterTypeEnum.DATE_RANGE,
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
                                        null
                                )),
                                null,
                                null
                        ));
    }
}