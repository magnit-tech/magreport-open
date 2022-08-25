package ru.magnit.magreportbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobUserTypeEnum;
import ru.magnit.magreportbackend.domain.user.SystemRoles;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.dto.request.reportjob.ExcelReportRequest;
import ru.magnit.magreportbackend.dto.request.reportjob.ReportJobAddRequest;
import ru.magnit.magreportbackend.dto.request.reportjob.ReportJobRequest;
import ru.magnit.magreportbackend.dto.request.reportjob.ReportJobShareRequest;
import ru.magnit.magreportbackend.dto.request.reportjob.ReportPageRequest;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportResponse;
import ru.magnit.magreportbackend.dto.response.reportjob.ReportJobMetadataResponse;
import ru.magnit.magreportbackend.dto.response.reportjob.ReportJobResponse;
import ru.magnit.magreportbackend.dto.response.reportjob.ReportPageResponse;
import ru.magnit.magreportbackend.dto.response.reportjob.ReportSqlQueryResponse;
import ru.magnit.magreportbackend.dto.response.reportjob.TokenResponse;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.exception.PermissionDeniedException;
import ru.magnit.magreportbackend.service.domain.AvroReportDomainService;
import ru.magnit.magreportbackend.service.domain.ExcelReportDomainService;
import ru.magnit.magreportbackend.service.domain.ExcelTemplateDomainService;
import ru.magnit.magreportbackend.service.domain.FilterReportDomainService;
import ru.magnit.magreportbackend.service.domain.FolderDomainService;
import ru.magnit.magreportbackend.service.domain.FolderPermissionsDomainService;
import ru.magnit.magreportbackend.service.domain.JobDomainService;
import ru.magnit.magreportbackend.service.domain.OlapConfigurationDomainService;
import ru.magnit.magreportbackend.service.domain.ReportDomainService;
import ru.magnit.magreportbackend.service.domain.ReportJobUserDomainService;
import ru.magnit.magreportbackend.service.domain.TokenService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReportJobService {

    private static final String ERROR_TEXT_HEADER = "Значения для обязательного фильтра id:";
    private static final String ERROR_TEXT_FOOTER = " не заданы.";

    private final JobDomainService jobDomainService;
    private final ExcelReportDomainService excelReportDomainService;
    private final AvroReportDomainService avroReportDomainService;
    private final ReportDomainService reportDomainService;
    private final FilterReportDomainService filterReportDomainService;
    private final ExcelTemplateDomainService excelTemplateDomainService;
    private final FolderDomainService folderDomainService;
    private final FolderPermissionsDomainService folderPermissionsDomainService;
    private final UserDomainService userDomainService;
    private final TokenService tokenService;
    private final ReportJobUserDomainService reportJobUserDomainService;

    private final OlapConfigurationDomainService olapConfigurationDomainService;

    public TokenResponse createExcelReport(ExcelReportRequest request) {

        final var jobData = jobDomainService.getJobData(request.getId());
        final var templatePath = excelTemplateDomainService.getTemplatePathForReport(jobData.reportData().id(), request.getExcelTemplateId());

        excelReportDomainService.createExcelReport(jobData, templatePath, request.getExcelTemplateId());

        return new TokenResponse(tokenService.getToken(request.getId(), request.getExcelTemplateId()));
    }

    public StreamingResponseBody getExcelReport(Long jobId, Long templateId) {
        final var jobData = jobDomainService.getJobData(jobId);
        final var templatePath = excelTemplateDomainService.getTemplatePathForReport(jobData.reportData().id(), templateId);

        excelReportDomainService.createExcelReport(jobData, templatePath, templateId);

        return excelReportDomainService.getExcelReport(jobData, templatePath, templateId);
    }

    public Path getExcelReportPath(Long jobId, Long templateId) {
        final var jobData = jobDomainService.getJobData(jobId);
        final var templatePath = excelTemplateDomainService.getTemplatePathForReport(jobData.reportData().id(),templateId);

        excelReportDomainService.createExcelReport(jobData, templatePath, templateId);

        return excelReportDomainService.getExcelReportPath(jobId,templateId);
    }

    public long getReportSize(Long jobId, Long templateId) {
        final var jobData = jobDomainService.getJobData(jobId);
        return excelReportDomainService.getReportSize(jobData.id(), templateId);
    }

    public void saveExcelReport(ExcelReportRequest request) {

        final var jobData = jobDomainService.getJobData(request.getId());

        if (request.getExcelTemplateId() == null) {
            request.setExcelTemplateId(excelTemplateDomainService.getDefaultExcelTemplateToReport(jobData.reportId()));
        }

        final var templatePath = excelTemplateDomainService.getTemplatePathForReport(jobData.reportData().id(), request.getExcelTemplateId());

        excelReportDomainService.saveReportToExcel(jobData, templatePath, request.getExcelTemplateId());
        excelReportDomainService.moveReportToRms(request.getId(), request.getExcelTemplateId());
    }

    public ReportJobResponse addJob(ReportJobAddRequest request) {

        final var report = reportDomainService.getReport(request.getReportId());

        if (!checkReportPermission(report.getId())) {
            throw new PermissionDeniedException("У Вас нет прав на выполнение отчета <" + report.getName() + ">, id:" + report.getId());
        }

        stripEmptyFilters(request);
        checkMandatoryFilters(request, report);
        checkDateParameters(request, report);

        Long jobId = jobDomainService.addJob(request);

        return jobDomainService.getJob(jobId);
    }

    private boolean checkReportPermission(Long reportId) {
        final var folderIds = folderDomainService.getReportsFolders(reportId);
        final var userRoles = userDomainService.getCurrentUserRoles(null);
        if (folderIds.isEmpty()) {
            return userRoles.stream().anyMatch(role -> role.getName().equals(SystemRoles.DEVELOPER.name()) || role.getName().equals(SystemRoles.ADMIN.name()));
        }
        final var roleIds = userRoles.stream().map(RoleView::getId).toList();
        final var permissions = folderPermissionsDomainService.getFoldersReportPermissionsForRoles(folderIds, roleIds);
        return !permissions.isEmpty();
    }

    private void checkDateParameters(ReportJobAddRequest request, ReportResponse report) {
        final var datePattern = "\\d{4}-\\d{2}-\\d{2}";
        final var checkResult = report
            .getAllFilters()
            .stream()
            .filter(filter -> filter.type() == FilterTypeEnum.DATE_RANGE || filter.type() == FilterTypeEnum.DATE_VALUE)
            .allMatch(filter -> request
                .getParameters()
                .stream()
                .filter(param -> param.getFilterId().equals(filter.id()))
                .flatMap(param -> param.getParameters().stream())
                .flatMap(tuple -> tuple.getValues().stream())
                .allMatch(value -> value.getValue().matches(datePattern))
            );
        if (!checkResult) throw new InvalidParametersException("Неправильный формат даты в параметрах");
    }

    private void stripEmptyFilters(ReportJobAddRequest request) {

        request
            .getParameters()
            .removeIf(parameter -> Objects.isNull(parameter.getParameters()));

        request
            .getParameters()
            .removeIf(parameter -> parameter.getParameters().isEmpty());

        request
            .getParameters()
            .removeIf(parameter -> parameter.getParameters().stream().mapToLong(tuple -> tuple.getValues().size()).sum() == 0);


    }

    private void checkMandatoryFilters(ReportJobAddRequest request, ReportResponse report) {
        final var allFilters = report.getAllFilters();

        allFilters
            .stream()
            .filter(FilterReportResponse::mandatory)
            .forEach(filter -> {
                if (request.getParameters().stream().noneMatch(o -> o.getFilterId().equals(filter.id())))
                    throw new InvalidParametersException(ERROR_TEXT_HEADER + filter.id() + " '" + filter.name() + ERROR_TEXT_FOOTER);
                if (filter.type() == FilterTypeEnum.VALUE_LIST) {
                    final var paramValues = request.getParameters().stream().filter(params -> params.getFilterId().equals(filter.id())).findFirst().orElseThrow(() -> new InvalidParametersException(ERROR_TEXT_HEADER + filter.id() + " '" + filter.name() + ERROR_TEXT_FOOTER));
                    final var values = filterReportDomainService.getCleanedValueListValues(paramValues);
                    if (values.isEmpty())
                        throw new InvalidParametersException(ERROR_TEXT_HEADER + filter.id() + " '" + filter.name() + ERROR_TEXT_FOOTER);
                }
            });
    }

    public ReportJobResponse getJob(ReportJobRequest request) {

        var response = jobDomainService.getJob(request.getJobId());
        response.setCanExecute(checkReportPermission(response.getReport().id()));
        return response;
    }

    public ReportJobResponse getJob(Long jobId) {
        return jobDomainService.getJob(jobId);
    }

    public List<ReportJobResponse> getMyJobs() {

        var responses = jobDomainService.getMyJobs();
        responses.forEach(response ->  response.setCanExecute(checkReportPermission(response.getReport().id())));
        return responses;

    }

    public List<ReportJobResponse> getAllJobs() {

        var responses =  jobDomainService.getAllJobs();
        responses.forEach(response ->  response.setCanExecute(checkReportPermission(response.getReport().id())));
        return responses;
    }

    public ReportPageResponse getReportPage(ReportPageRequest request) {

        var jobData = jobDomainService.getJobData(request.getJobId());

        if (jobData.isComplete()) {
            return avroReportDomainService.getPage(jobData, request.getPageNumber(), request.getRowsPerPage());
        }

        return null;
    }

    public ReportJobResponse cancelJob(ReportJobRequest request) {

        jobDomainService.cancelJob(request.getJobId());

        return jobDomainService.getJob(request.getJobId());
    }

    public void deleteJob(ReportJobRequest request) {
        jobDomainService.deleteJob(request.getJobId());
    }

    public void deleteAllJobs() {
        jobDomainService.deleteAllJobs();
    }

    public ReportSqlQueryResponse getSqlQuery(ReportJobRequest request) {
        return jobDomainService.getSqlQuery(request.getJobId());
    }

    public Path getPathToExcelReport(ExcelReportRequest request) {
        return excelReportDomainService.getExcelReportPath(request.getId(), request.getExcelTemplateId());
    }

    public ReportJobMetadataResponse getMetaData(ReportJobRequest request) {
        return jobDomainService.getJobMetaData(request.getJobId());
    }

    public void shareJob(ReportJobShareRequest request){

        var currentUser = userDomainService.getCurrentUser();
        var users = request.getUsers().stream().map(userDomainService::getUserResponse).toList();
        reportJobUserDomainService.addUsersJob(ReportJobUserTypeEnum.SHARE, request.getJobId(),users);
        olapConfigurationDomainService.createCurrentConfigurationForUsers(users.stream().map(UserResponse::getId).toList(),request.getJobId(), currentUser.getId());

    }

    public void deleteShareJob(ReportJobShareRequest request){
        var users = request.getUsers().stream().map(userDomainService::getUserResponse).toList();
        reportJobUserDomainService.deleteUsersJob(ReportJobUserTypeEnum.SHARE, request.getJobId(),users);
        olapConfigurationDomainService.deleteCurrentConfigurationUsers(users.stream().map(UserResponse::getId).toList(),request.getJobId());
    }

    public List<UserResponse> getUsersJob(ReportJobRequest request){
       return reportJobUserDomainService.getUsersJob(request.getJobId());
    }
}
