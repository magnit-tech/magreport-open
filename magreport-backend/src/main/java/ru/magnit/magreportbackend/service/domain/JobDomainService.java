package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.reportjob.ReportJob;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobState;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobStateEnum;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobStatus;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobStatusEnum;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobUserTypeEnum;
import ru.magnit.magreportbackend.domain.reportjobstats.ReportJobStatistics;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobData;
import ru.magnit.magreportbackend.dto.request.ChangeParentFolderRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportIdRequest;
import ru.magnit.magreportbackend.dto.request.reportjob.ReportJobAddRequest;
import ru.magnit.magreportbackend.dto.response.report.ReportJobFilterResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportResponse;
import ru.magnit.magreportbackend.dto.response.reportjob.ReportJobMetadataResponse;
import ru.magnit.magreportbackend.dto.response.reportjob.ReportJobResponse;
import ru.magnit.magreportbackend.dto.response.reportjob.ReportSqlQueryResponse;
import ru.magnit.magreportbackend.exception.FileSystemException;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.reportjob.ReportJobDataMapper;
import ru.magnit.magreportbackend.mapper.reportjob.ReportJobFilterResponseMapper;
import ru.magnit.magreportbackend.mapper.reportjob.ReportJobMapper;
import ru.magnit.magreportbackend.mapper.reportjob.ReportJobMetadataResponseMapper;
import ru.magnit.magreportbackend.mapper.reportjob.ReportJobResponseMapper;
import ru.magnit.magreportbackend.mapper.reportjob.ReportJobResponseTupleMapper;
import ru.magnit.magreportbackend.repository.ReportJobFilterRepository;
import ru.magnit.magreportbackend.repository.ReportJobRepository;
import ru.magnit.magreportbackend.repository.ReportJobStatisticsRepository;
import ru.magnit.magreportbackend.repository.ReportJobUserRepository;
import ru.magnit.magreportbackend.service.ExcelTemplateService;
import ru.magnit.magreportbackend.service.PermissionCheckerSystem;
import ru.magnit.magreportbackend.util.FileUtils;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobDomainService {

    private final ReportJobRepository repository;
    private final UserDomainService userDomainService;
    private final ReportJobStatisticsRepository statisticsRepository;
    private final ReportJobFilterRepository reportJobFilterRepository;
    private final ReportJobUserRepository reportJobUserRepository;
    private final PermissionCheckerSystem permissionCheckerSystem;
    private final FolderPermissionsDomainService folderPermissionsDomainService;


    private final ReportJobMapper reportJobMapper;
    private final ReportJobDataMapper reportJobDataMapper;
    private final ExcelTemplateService excelTemplateService;
    private final ReportJobResponseMapper reportJobResponseMapper;
    private final ReportJobResponseTupleMapper reportJobResponseTupleMapper;
    private final ReportJobFilterResponseMapper reportJobFilterResponseMapper;
    private final ReportJobMetadataResponseMapper reportJobMetadataResponseMapper;

    @Value("${magreport.jobengine.job-retention-time}")
    private Long jobRetentionTime;

    @Value("${magreport.reports.folder}")
    private String reportFolder;

    @Value("${magreport.reports.rms-out-folder}")
    private String rmsOutFolder;

    @Value("${magreport.jobengine.clean-rms-out-folder}")
    private Boolean clearRmsOutFolder;

    @Transactional
    public ReportJobData getNextScheduledJob() {
        var job = repository.getFirstByStatusIdAndStateIdOrderById(
                ReportJobStatusEnum.SCHEDULED.getId(),
                ReportJobStateEnum.NORMAL.getId());

        return job == null ? null : reportJobDataMapper.from(job);
    }

    @Transactional
    public void setJobStatus(Long id, ReportJobStatusEnum status, Long rowCount, String errorMessage, String sqlQuery) {

        final var job = repository.getReferenceById(id);
        if (checkFinalStatus(job.getStatus().getId())) {
            job.setStatus(new ReportJobStatus(status.getId()));
            job.setRowCount(rowCount);
            job.setMessage(errorMessage);
            job.setSqlQuery(sqlQuery);
            repository.save(job);

            saveStats(job);
        }
    }

    @Transactional
    public void setJobStatus(Long id, ReportJobStatusEnum status, Long rowCount, String errorMessage) {

        final var job = repository.getReferenceById(id);
        if (checkFinalStatus(job.getStatus().getId())) {
            job.setStatus(new ReportJobStatus(status.getId()));
            job.setRowCount(rowCount);
            job.setMessage(errorMessage);
            repository.save(job);

            saveStats(job);
        }
    }

    @Transactional
    public void setJobStatus(Long id, ReportJobStatusEnum status, Long rowCount) {
        setJobStatus(id, status, rowCount, null);
    }

    @Transactional
    public ReportJobData getJobData(Long jobId) {
        var job = repository.getReferenceById(jobId);
        return reportJobDataMapper.from(job);
    }

    @Transactional
    public boolean isJobFinished(Long id) {
        var job = repository.getReferenceById(id);
        return ReportJobStatusEnum.getById(job.getStatus().getId()) == ReportJobStatusEnum.COMPLETE;
    }

    @Transactional
    public Long addJob(ReportJobAddRequest request) {
        var job = reportJobMapper.from(request);
        job.setUser(new User(userDomainService.getCurrentUser().getId()));
        job = repository.save(job);
        saveStats(job);
        return job.getId();
    }

    @Transactional
    public Long addJob(ReportResponse report, List<ReportJobFilterResponse> jobFilters, Long userId) {

        final var job = new ReportJob();
        job.setReport(new Report(report.getId()));
        job.setReportJobFilters(jobFilters.stream().map(f -> reportJobFilterRepository.getReferenceById(f.getId())).toList());
        job.setUser(new User(userId));
        job.setStatus(new ReportJobStatus(ReportJobStatusEnum.SCHEDULED.getId()));
        job.setState(new ReportJobState(ReportJobStateEnum.NORMAL.getId()));
        job.setRowCount(0L);
        job.getReportJobFilters().forEach(filter -> filter.setReportJob(job));
        var saveJob = repository.save(job);

        saveStats(saveJob);

        return saveJob.getId();
    }

    private void saveStats(ReportJob job) {
        var jobStats = new ReportJobStatistics()
                .setReportJob(new ReportJob(job.getId()))
                .setReport(new Report(job.getReport().getId()))
                .setUser(new User(job.getUser().getId()))
                .setRowCount(job.getRowCount())
                .setStatus(job.getStatus())
                .setState(job.getState());

        statisticsRepository.save(jobStats);
    }

    @Transactional
    public ReportJobResponse getJob(Long jobId) {
        var job = repository.findById(jobId);
        return job.map(reportJobResponseMapper::from).orElse(null);
    }

    @Transactional
    public List<ReportJobResponse> getMyJobs() {
        final var currentUser = userDomainService.getCurrentUser();
        var response = reportJobResponseMapper.from(repository.getAllByUserId2(currentUser.getId()));

        response.forEach(r ->
                r.getExcelTemplates().addAll(
                        excelTemplateService.getAllReportExcelTemplateToReport(new ReportIdRequest().setId(r.getReport().id()))));

        return response;
    }

    @Transactional
    public List<ReportJobResponse> getAllJobs() {
        var result = new ArrayList<ReportJobResponse>();

        repository.getAllJobWithTemplate().stream()
                .map(reportJobResponseTupleMapper::from)
                .collect(Collectors.groupingBy(ReportJobResponse::getId))
                .forEach((id, reportJobResponses) -> {
                    var item = reportJobResponses.get(0);
                    reportJobResponses.remove(0);
                    reportJobResponses.forEach(reportJobResponse ->
                            item.getExcelTemplates().addAll(reportJobResponse.getExcelTemplates()));
                    result.add(item);
                });

        return result;
    }

    @Transactional
    public void cancelJob(Long jobId) {
        setJobStatus(jobId, ReportJobStatusEnum.CANCELING, -1L, null);
    }

    @Transactional
    public List<ReportJobResponse> getJobs(List<Long> jobIds) {
        if (jobIds.isEmpty()) return Collections.emptyList();
        return reportJobResponseMapper.from(repository.getAllByIdIn(jobIds));
    }

    @Transactional
    public List<ReportJobFilterResponse> getLastJobParameters(Long userId, Long reportId) {

        final var lastJob = repository.getFirstByUserIdAndReportIdOrderByIdDesc(userId, reportId);
        if (lastJob == null) return Collections.emptyList();

        return reportJobFilterResponseMapper.from(lastJob.getReportJobFilters());
    }

    @Transactional
    public List<ReportJobFilterResponse> getJobParameters(Long jobId) {

        final var lastJob = repository.existsById(jobId) ? repository.getReferenceById(jobId) : null;
        if (lastJob == null) return Collections.emptyList();

        return reportJobFilterResponseMapper.from(lastJob.getReportJobFilters());
    }

    @Transactional
    public List<ReportJobFilterResponse> getScheduleJobParameters(Long scheduleTaskId) {
        if (scheduleTaskId != null) {
            var jobFilters = reportJobFilterRepository.findAllByScheduleTaskId(scheduleTaskId);
            return reportJobFilterResponseMapper.from(jobFilters);
        } else
            return Collections.emptyList();
    }

    @Transactional
    public void deleteOldJobs() {
        log.debug("Clearing of the old job data started..");
        var lastDate = LocalDateTime.now().minusHours(jobRetentionTime);
        final var oldJobs = repository.findAllByCreatedDateTimeBefore(lastDate);
        oldJobs.forEach(oldJob -> {
            try {
                Files.deleteIfExists(Path.of(FileUtils.replaceHomeShortcut(reportFolder) + "/" + oldJob.getId() + ".avro"));
            } catch (IOException ex) {
                throw new FileSystemException("Error trying to delete report file for job with id:" + oldJob.getId(), ex);
            }
        });

        if (Boolean.TRUE.equals(clearRmsOutFolder)) {
            var removeFiles = Arrays.stream(Objects.requireNonNull(new File(FileUtils.replaceHomeShortcut(rmsOutFolder))
                    .listFiles()))
                .filter(file -> new Date(file.lastModified()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().isBefore(LocalDateTime.now().minusHours(jobRetentionTime)))
                .map(file -> Path.of(file.getAbsolutePath())).toList();

            removeFiles.forEach(file -> {
                try {
                    Files.deleteIfExists(file);
                } catch (IOException ex) {
                    throw new FileSystemException("Error trying to delete report file for name:" + file.getFileName(), ex);
                }
            });
        }
        repository.deleteAll(oldJobs);
        log.debug("Clearing of the old job data complete");
    }

    @Transactional
    public void refreshStatusesAfterRestart() {
        final var runningJobs = repository.findAllByStatusIdAndStateId(ReportJobStatusEnum.RUNNING.getId(), ReportJobStateEnum.NORMAL.getId());
        runningJobs.forEach(job -> job.setStatus(new ReportJobStatus(ReportJobStatusEnum.SCHEDULED.getId())));
        repository.saveAll(runningJobs);
        final var cancelingJobs = repository.findAllByStatusIdAndStateId(ReportJobStatusEnum.CANCELING.getId(), ReportJobStateEnum.NORMAL.getId());
        cancelingJobs.forEach(job -> job.setStatus(new ReportJobStatus(ReportJobStatusEnum.CANCELED.getId())));
        repository.saveAll(cancelingJobs);
    }

    @Transactional
    public void deleteJob(Long jobId) {
        repository.deleteById(jobId);
    }

    @Transactional
    public void deleteAllJobs() {
        repository.deleteAll();
    }

    @Transactional
    public ReportSqlQueryResponse getSqlQuery(Long idJob) {
        var job = repository.getReferenceById(idJob);
        return new ReportSqlQueryResponse(idJob, job.getSqlQuery());
    }

    @Transactional
    public ReportJobMetadataResponse getJobMetaData(Long jobId) {
        final var job = repository.getReferenceById(jobId);
        return reportJobMetadataResponseMapper.from(job);
    }

    @Transactional
    public void checkAccessForJob(Long jobId) {

        var job = repository.getReferenceById(jobId);
        var currentUser = userDomainService.getCurrentUser();

        if (currentUser.getId().equals(job.getUser().getId())) return;

        var sharedJob = reportJobUserRepository.findSharedJob(jobId, currentUser.getId(), ReportJobUserTypeEnum.SHARE.getId());
        if (sharedJob.isEmpty()) throw new InvalidParametersException("Permission denied");

        var request = new ChangeParentFolderRequest();
        request.setDestFolderId(job.getReport().getFolder().getId());
        permissionCheckerSystem.checkPermissionsOnAllFolders(request, null, folderPermissionsDomainService::getReportFolderPermissionsForRoles);
    }


    private boolean checkFinalStatus(Long status) {
        return !status.equals(ReportJobStatusEnum.FAILED.getId()) &&
                !status.equals(ReportJobStatusEnum.COMPLETE.getId()) &&
                !status.equals(ReportJobStatusEnum.CANCELED.getId());
    }
}
