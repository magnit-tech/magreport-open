package ru.magnit.magreportbackend.service.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum;
import ru.magnit.magreportbackend.domain.filterreport.GroupOperationTypeEnum;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.reportjob.ReportJob;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobState;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobStateEnum;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobStatus;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobStatusEnum;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportFilterGroupData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobData;
import ru.magnit.magreportbackend.dto.request.reportjob.ReportJobAddRequest;
import ru.magnit.magreportbackend.dto.response.report.ReportShortResponse;
import ru.magnit.magreportbackend.dto.response.reportjob.ReportJobResponse;
import ru.magnit.magreportbackend.dto.response.user.UserShortResponse;
import ru.magnit.magreportbackend.mapper.reportjob.ReportJobDataMapper;
import ru.magnit.magreportbackend.mapper.reportjob.ReportJobFilterResponseMapper;
import ru.magnit.magreportbackend.mapper.reportjob.ReportJobMapper;
import ru.magnit.magreportbackend.mapper.reportjob.ReportJobResponseMapper;
import ru.magnit.magreportbackend.mapper.reportjob.ReportJobResponseTupleMapper;
import ru.magnit.magreportbackend.repository.ReportJobRepository;
import ru.magnit.magreportbackend.repository.ReportJobStatisticsRepository;
import ru.magnit.magreportbackend.service.ExcelTemplateService;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobDomainServiceTest {

    @Mock
    private ReportJobRepository repository;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private ReportJobStatisticsRepository statisticsRepository;

    @Mock
    private ReportJobDataMapper reportJobDataMapper;

    @Mock
    private ReportJobMapper reportJobMapper;

    @Mock
    private ReportJobResponseMapper responseMapper;

    @Mock
    private ExcelTemplateService excelTemplateService;

    @Mock
    private ReportJobFilterResponseMapper reportJobFilterResponseMapper;

    @Mock
    private ReportJobResponseTupleMapper reportJobResponseTupleMapper;

    @InjectMocks
    private JobDomainService domainService;

    private final static Long ID = 1L;
    private final static String NAME = "name";
    private final static String DESCRIPTION = "description";
    private final static LocalDateTime NOW = LocalDateTime.now();

    @Test
    void getNextScheduledJob() {

        when(repository.getFirstByStatusIdAndStateIdOrderById(any(), any())).thenReturn(new ReportJob());
        when(reportJobDataMapper.from(any(ReportJob.class))).thenReturn(getReportJobData());

        assertNotNull(domainService.getNextScheduledJob());

        verify(repository).getFirstByStatusIdAndStateIdOrderById(any(), any());
        verify(reportJobDataMapper).from(any(ReportJob.class));

        verifyNoMoreInteractions(reportJobDataMapper, repository);
    }

    @Test
    void setJobStatus1() {

        when(repository.getReferenceById(ID)).thenReturn(getReportJob());

        domainService.setJobStatus(ID, ReportJobStatusEnum.RUNNING, 100L, "", "");

        verify(repository).getReferenceById(anyLong());
        verify(repository).save(any());
        verify(statisticsRepository).save(any());
        verifyNoMoreInteractions(repository, statisticsRepository);
    }

    @Test
    void setJobStatus2() {

        when(repository.getReferenceById(ID)).thenReturn(getReportJob());

        domainService.setJobStatus(ID, ReportJobStatusEnum.RUNNING, 100L, "");

        verify(repository).getReferenceById(anyLong());
        verify(repository).save(any());
        verify(statisticsRepository).save(any());
        verifyNoMoreInteractions(repository, statisticsRepository);

    }

    @Test
    void setJobStatus3() {
        when(repository.getReferenceById(ID)).thenReturn(getReportJob());

        domainService.setJobStatus(ID, ReportJobStatusEnum.RUNNING, 100L);

        verify(repository).getReferenceById(anyLong());
        verify(repository).save(any());
        verify(statisticsRepository).save(any());
        verifyNoMoreInteractions(repository, statisticsRepository);

    }

    @Test
    void getJobData() {

        when(repository.getReferenceById(anyLong())).thenReturn(getReportJob());
        when(reportJobDataMapper.from(any(ReportJob.class))).thenReturn(getReportJobData());

        assertNotNull(domainService.getJobData(ID));


        verify(repository).getReferenceById(anyLong());
        verify(reportJobDataMapper).from(any(ReportJob.class));
        verifyNoMoreInteractions(repository, reportJobDataMapper);
    }

    @Test
    void isJobFinished() {
        when(repository.getReferenceById(anyLong())).thenReturn(getReportJob());

        assertFalse(domainService.isJobFinished(ID));

        verify(repository).getReferenceById(anyLong());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void addJob() {

        when(reportJobMapper.from(any(ReportJobAddRequest.class))).thenReturn(getReportJob());
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setId(ID));
        when(repository.save(any())).thenReturn(getReportJob());

        assertNotNull(domainService.addJob(new ReportJobAddRequest()));

        verify(reportJobMapper).from(any(ReportJobAddRequest.class));
        verify(userDomainService).getCurrentUser();
        verify(repository).save(any());
        verifyNoMoreInteractions(userDomainService, reportJobMapper);
    }

    @Test
    void getJob() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(getReportJob()));
        when(responseMapper.from(any(ReportJob.class))).thenReturn(getReportJobResponse());

        assertNotNull(domainService.getJob(ID));

        verify(repository).findById(anyLong());
        verify(responseMapper).from(any(ReportJob.class));
        verifyNoMoreInteractions(repository, responseMapper);
    }

    @Test
    void getMyJobs() {
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setId(ID));
        when(responseMapper.from(anyList())).thenReturn(Collections.singletonList(getReportJobResponse()));
        when(repository.getAllByUserId2(any())).thenReturn(Collections.emptyList());
        when(excelTemplateService.getAllReportExcelTemplateToReport(any())).thenReturn(Collections.emptyList());

        assertNotNull(domainService.getMyJobs());

        verify(userDomainService).getCurrentUser();
        verify(responseMapper).from(anyList());
        verify(repository).getAllByUserId2(any());
        verify(excelTemplateService).getAllReportExcelTemplateToReport(any());
        verifyNoMoreInteractions(userDomainService, responseMapper, repository, excelTemplateService);

    }

    @Test
    void getAllJobs() {
        when(reportJobResponseTupleMapper.from(any(Tuple.class))).thenReturn(getReportJobResponse());
        when(repository.getAllJobWithTemplate()).thenReturn(Collections.singletonList(getTuple()));

        assertNotNull(domainService.getAllJobs());

        verify(reportJobResponseTupleMapper).from(any(Tuple.class));
        verify(repository).getAllJobWithTemplate();
        verifyNoMoreInteractions(reportJobResponseTupleMapper, repository);
    }

    @Test
    void cancelJob() {

        when(repository.getReferenceById(ID)).thenReturn(getReportJob());

        domainService.cancelJob(ID);

        verify(repository).getReferenceById(anyLong());
        verify(repository).save(any());
        verify(statisticsRepository).save(any());
        verifyNoMoreInteractions(repository, statisticsRepository);
    }

    @Test
    void getJobs() {

        assertTrue(domainService.getJobs(Collections.emptyList()).isEmpty());

        when(repository.getAllByIdIn(anyList())).thenReturn(Collections.singletonList(getReportJob()));
        when(responseMapper.from(anyList())).thenReturn(Collections.singletonList(getReportJobResponse()));

        assertNotNull(domainService.getJobs(Collections.singletonList(ID)));

        verify(repository).getAllByIdIn(anyList());
        verify(responseMapper).from(anyList());
        verifyNoMoreInteractions(repository, responseMapper);
    }

    @Test
    void getLastJobParameters() {

        when(repository.getFirstByUserIdAndReportIdOrderByIdDesc(any(), any())).thenReturn(null);

        assertTrue(domainService.getLastJobParameters(ID, ID).isEmpty());

        verify(repository).getFirstByUserIdAndReportIdOrderByIdDesc(any(), any());
        verifyNoMoreInteractions(repository);

        Mockito.reset(repository);

        when(repository.getFirstByUserIdAndReportIdOrderByIdDesc(any(), any())).thenReturn(getReportJob());
        when(reportJobFilterResponseMapper.from(anyList())).thenReturn(Collections.emptyList());

        assertNotNull(domainService.getLastJobParameters(ID, ID));

    }

    @Test
    void getJobParameters() {
        when(repository.existsById(any())).thenReturn(false);
        assertTrue(domainService.getJobParameters(ID).isEmpty());

        verify(repository).existsById(any());
        verifyNoMoreInteractions(repository);

        Mockito.reset(repository);
        when(repository.existsById(any())).thenReturn(true);
        when(repository.getReferenceById(anyLong())).thenReturn(getReportJob());
        when(reportJobFilterResponseMapper.from(anyList())).thenReturn(Collections.emptyList());

        assertNotNull(domainService.getJobParameters(ID));

        verify(repository).existsById(any());
        verify(repository).getReferenceById(anyLong());
        verify(reportJobFilterResponseMapper).from(anyList());
        verifyNoMoreInteractions(repository, reportJobFilterResponseMapper);
    }

    @Test
    void deleteOldJobs() {

        ReflectionTestUtils.setField(domainService, "jobRetentionTime", 336L);
        ReflectionTestUtils.setField(domainService, "reportFolder", "");
        ReflectionTestUtils.setField(domainService, "clearRmsOutFolder", false);
        ReflectionTestUtils.setField(domainService, "rmsOutFolder", "");
        when(repository.findAllByCreatedDateTimeBefore(any())).thenReturn(Collections.singletonList(getReportJob()));
        domainService.deleteOldJobs();

        verify(repository).findAllByCreatedDateTimeBefore(any());
        verify(repository).deleteAll(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void refreshStatusesAfterRestart() {
        when(repository.findAllByStatusIdAndStateId(any(), any())).thenReturn(Collections.singletonList(getReportJob()));

        domainService.refreshStatusesAfterRestart();

        verify(repository, times(2)).saveAll(anyList());
        verify(repository, times(2)).findAllByStatusIdAndStateId(any(), any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deleteJob() {

        domainService.deleteJob(any());

        verify(repository).deleteById(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deleteAllJobs() {

        domainService.deleteAllJobs();

        verify(repository).deleteAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    void getSqlQuery() {
        when(repository.getReferenceById(anyLong())).thenReturn(getReportJob());

        domainService.getSqlQuery(ID);

        verify(repository).getReferenceById(anyLong());
        verifyNoMoreInteractions(repository);
    }


    private ReportJobData getReportJobData() {
        return new ReportJobData(
                ID, ID, ID, ID, ID, NAME, ID, ID, ID, true,
                new DataSourceData(ID, DataSourceTypeEnum.TERADATA, "URL", NAME, "PWD", (short) 1),
                new ReportData(ID, NAME, DESCRIPTION, "SCHEMA", "TABLE", Collections.emptyList(),
                        new ReportFilterGroupData(ID,
                                null,
                                "CODE",
                                "PARENT_CODE",
                                GroupOperationTypeEnum.AND,
                                Collections.emptyList(),
                                Collections.emptyList())),
                Collections.emptyList(),
                Collections.emptyList()
        );
    }

    private ReportJob getReportJob() {
        return new ReportJob()
                .setId(ID)
                .setUser(new User().setId(ID))
                .setReport(new Report())
                .setReportJobFilters(new ArrayList<>())
                .setMessage("message")
                .setSqlQuery("query")
                .setRowCount(1000L)
                .setState(new ReportJobState())
                .setStatus(new ReportJobStatus().setId(ID))
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW);
    }

    private ReportJobResponse getReportJobResponse() {
        return new ReportJobResponse(
                ID,
                new ReportShortResponse(ID, NAME),
                new UserShortResponse(ID, NAME),
                ReportJobStatusEnum.RUNNING,
                ReportJobStateEnum.NORMAL,
                "message",
                1000L,
                NOW,
                NOW,
                Collections.emptyList(),
                false
        );
    }

    private Tuple getTuple() {
        return new Tuple() {
            @Override
            public <X> X get(TupleElement<X> tupleElement) {
                return null;
            }

            @Override
            public <X> X get(String s, Class<X> aClass) {
                return null;
            }

            @Override
            public Object get(String s) {
                return null;
            }

            @Override
            public <X> X get(int i, Class<X> aClass) {
                return null;
            }

            @Override
            public Object get(int i) {
                return null;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public List<TupleElement<?>> getElements() {
                return null;
            }
        };
    }
}
