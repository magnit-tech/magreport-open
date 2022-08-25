package ru.magnit.magreportbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTaskStatusEnum;
import ru.magnit.magreportbackend.domain.user.SystemRoles;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.dto.inner.filter.FilterRequestData;
import ru.magnit.magreportbackend.dto.request.ChangeParentFolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.CopyFolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderTypes;
import ru.magnit.magreportbackend.dto.request.folderreport.FolderPermissionSetRequest;
import ru.magnit.magreportbackend.dto.request.folderreport.RoleAddPermissionRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportAddFavoritesRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportAddRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportEditRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportIdRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportRequest;
import ru.magnit.magreportbackend.dto.request.report.ScheduleReportRequest;
import ru.magnit.magreportbackend.dto.response.folder.FolderRoleResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResponse;
import ru.magnit.magreportbackend.dto.response.folderreport.FolderResponse;
import ru.magnit.magreportbackend.dto.response.report.PivotFieldTypeResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportFolderResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportJobFilterResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportResponse;
import ru.magnit.magreportbackend.dto.tuple.Tuple;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.report.ReportResponseMapper;
import ru.magnit.magreportbackend.repository.ReportFolderRepository;
import ru.magnit.magreportbackend.service.domain.DataSetDomainService;
import ru.magnit.magreportbackend.service.domain.ExcelTemplateDomainService;
import ru.magnit.magreportbackend.service.domain.FilterReportDomainService;
import ru.magnit.magreportbackend.service.domain.FolderEntitySearchDomainService;
import ru.magnit.magreportbackend.service.domain.FolderPermissionsDomainService;
import ru.magnit.magreportbackend.service.domain.JobDomainService;
import ru.magnit.magreportbackend.service.domain.MailTextDomainService;
import ru.magnit.magreportbackend.service.domain.ReportDomainService;
import ru.magnit.magreportbackend.service.domain.ScheduleTaskDomainService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;
import ru.magnit.magreportbackend.service.jobengine.filter.FilterQueryExecutor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportDomainService reportDomainService;
    private final FilterReportDomainService filterReportDomainService;
    private final FilterReportService filterReportService;
    private final JobDomainService jobDomainService;
    private final FolderPermissionsDomainService folderPermissionsDomainService;
    private final UserDomainService userDomainService;
    private final DataSetDomainService dataSetDomainService;
    private final FilterQueryExecutor filterQueryExecutor;
    private final FolderEntitySearchDomainService folderEntitySearchDomainService;
    private final ExcelTemplateDomainService excelTemplateDomainService;
    private final ScheduleTaskDomainService scheduleTaskDomainService;
    private final MailTextDomainService mailTextDomainService;
    private final ReportFolderRepository reportFolderRepository;
    private final ReportResponseMapper reportResponseMapper;
    private final PermissionCheckerSystem permissionCheckerSystem;

    public ReportFolderResponse getFolder(FolderRequest request) {

        final var currentUser = userDomainService.getCurrentUser();
        final var folderResponse = reportDomainService.getFolder(currentUser.getId(), request.getId());

        final var folderIds = folderResponse.getChildFolders().stream().map(ReportFolderResponse::getId).collect(Collectors.toList());
        folderIds.add(folderResponse.getId());

        final var userRoles = userDomainService.getUserRoles(currentUser.getName(), null).stream().map(RoleView::getId).toList();
        final var foldersPermissions = folderPermissionsDomainService.getReportFolderPermissionsForRoles(folderIds, userRoles).stream().collect(Collectors.toMap(FolderRoleResponse::getFolderId, FolderRoleResponse::getAuthority));

        folderResponse.setAuthority(foldersPermissions.getOrDefault(folderResponse.getId(), FolderAuthorityEnum.NONE));
        folderResponse.getChildFolders()
                .forEach(folder -> folder.setAuthority(foldersPermissions.getOrDefault(folder.getId(), FolderAuthorityEnum.NONE)));

        if (userRoles.contains(SystemRoles.ADMIN.getId())) {
            folderResponse.setAuthority(FolderAuthorityEnum.WRITE);
            folderResponse.getChildFolders().forEach(folder -> folder.setAuthority(FolderAuthorityEnum.WRITE));
            return folderResponse;
        }

        if (folderResponse.getParentId() != null && folderResponse.getAuthority() == FolderAuthorityEnum.NONE)
            return null;

        folderResponse.getChildFolders().removeIf(folder -> folder.getAuthority() == FolderAuthorityEnum.NONE);

        return folderResponse;
    }

    public ReportFolderResponse addFolder(FolderAddRequest request) {
        final var currentFolderPermissions = request.getParentId() == null ?
                null :
                folderPermissionsDomainService.getReportFolderPermissions(request.getParentId());

        final var result = reportDomainService.addFolder(request);

        if (currentFolderPermissions != null) {
            folderPermissionsDomainService.setReportFolderPermissions(
                    Collections.singletonList(result.getId()),
                    new FolderPermissionSetRequest()
                            .setFolderId(result.getId())
                            .setRoles(currentFolderPermissions
                                    .rolePermissions()
                                    .stream()
                                    .map(rfp -> new RoleAddPermissionRequest()
                                            .setRoleId(rfp.role().getId())
                                            .setPermissions(rfp.permissions()))
                                    .toList()
                            ));
        }

        final var currentUser = userDomainService.getCurrentUser();

        return reportDomainService.getFolder(currentUser.getId(), result.getId());
    }

    public List<ReportFolderResponse> getChildFolders(FolderRequest request) {

        return reportDomainService.getChildFolders(request.getId());
    }

    public ReportFolderResponse renameFolder(FolderRenameRequest request) {

        return reportDomainService.renameFolder(request);
    }

    public void deleteFolder(FolderRequest request) {

        reportDomainService.deleteFolder(request.getId());
    }

    public ReportResponse addReport(ReportAddRequest request) {
        final var dataSet = dataSetDomainService.getDataSet(request.getDataSetId());

        if (Boolean.FALSE.equals(dataSet.getIsValid()))
            throw new InvalidParametersException("Набор данных имеет не существующие на источнике поля.");

        final var currentUser = userDomainService.getCurrentUser();
        var reportId = reportDomainService.addReport(currentUser, request);

        excelTemplateDomainService.setDefaultSystemExcelTemplateToReport(reportId);
        return reportDomainService.getReport(reportId);
    }

    public void addReportToFavorites(ReportAddFavoritesRequest request) {
        final var currentUser = userDomainService.getCurrentUser();
        reportDomainService.addReportToFavorites(request, currentUser);
    }

    public void deleteReport(ReportRequest request) {
        reportDomainService.deleteReport(request.getId());
        excelTemplateDomainService.removeReportExcelTemplate(request.getId());
        scheduleTaskDomainService.findScheduleTaskByReport(request.getId())
                .forEach(task -> {
                    scheduleTaskDomainService.setStatusScheduleTask(task.getId(), ScheduleTaskStatusEnum.CHANGED);
                    mailTextDomainService.sendScheduleMailChanged(task);
                });
    }

    public ReportResponse getReport(ReportRequest request) {

        final var report = reportDomainService.getReport(request.getId());

        try {
            final var lastParameters = request.getJobId() == null ?
                    jobDomainService.getLastJobParameters(userDomainService.getCurrentUser().getId(), request.getId())
                    :
                    jobDomainService.getJobParameters(request.getJobId());

            decodeTuples(lastParameters);

            report.setLastParameters(lastParameters);
        } catch (Exception ex) {
            log.error("\nError trying to fetch last parameters: " + ex.getMessage(), ex);
            report.setLastParameters(Collections.emptyList());
        }

        report.setExcelTemplates(excelTemplateDomainService.getAllReportExcelTemplateToReport(new ReportIdRequest().setId(request.getId())));
        return report;
    }

    public ReportResponse getScheduleReport(ScheduleReportRequest request) {

        final var report = reportDomainService.getReport(request.id());
        final List<ReportJobFilterResponse> lastParameters;
        if (request.scheduleTaskId() != null)
            lastParameters = jobDomainService.getScheduleJobParameters(request.scheduleTaskId());
        else
            lastParameters = jobDomainService.getLastJobParameters(userDomainService.getCurrentUser().getId(), request.id());

        decodeTuples(lastParameters);
        report.setLastParameters(lastParameters);
        return report;
    }

    public ReportResponse editReport(ReportEditRequest request) {

        if (request.getDescription().length() > 255 ||
                request.getName().length() > 255 ||
                request.getRequirementsLink().length() > 4096) {
            throw new InvalidParametersException("Длина поля превышает допустимые значения");
        }

        filterReportDomainService.removeFilters(request.getId());
        reportDomainService.deleteFields(reportDomainService.getDeletedFields(request));
        reportDomainService.editReport(request);

        if (request.getFilterGroup() != null)
            filterReportService.addFilters(request.getFilterGroup());

        return reportDomainService.getReport(request.getId());
    }

    public List<PivotFieldTypeResponse> getPivotFieldTypes() {
        return reportDomainService.getPivotFieldTypes();
    }

    public FolderSearchResponse searchFolder(FolderSearchRequest request) {
        return folderEntitySearchDomainService.search(request, reportFolderRepository, reportResponseMapper, FolderTypes.REPORT_FOLDER);
    }

    public ReportFolderResponse changeParentFolder(FolderChangeParentRequest request) {
        var roles = userDomainService.getUserRoles(userDomainService.getCurrentUser().getName(), null).stream().map(RoleView::getId).toList();

        folderPermissionsDomainService.getReportFolderPermissionsForRoles(Collections.singletonList(request.getId()), roles)
                .forEach(f -> {
                    if (f.getAuthority() == FolderAuthorityEnum.NONE)
                        throw new InvalidParametersException(String.format("Move for folder is not available with id %s: Permission denied", f.getFolderId()));

                });

        folderPermissionsDomainService.getReportFolderPermissionsForRoles(Collections.singletonList(request.getParentId()), roles)
                .forEach(f -> {
                    if (f.getAuthority() != FolderAuthorityEnum.WRITE)
                        throw new InvalidParametersException("Dest folder is not available: Permission denied");
                });

        return reportDomainService.changeParentFolder(request);
    }

    private void decodeTuples(List<ReportJobFilterResponse> lastParameters) {
        lastParameters
                .stream()
                .filter(filter -> filter.getFilterType() == FilterTypeEnum.TOKEN_INPUT || filter.getFilterType() == FilterTypeEnum.VALUE_LIST)
                .forEach(filter -> {
                    final var filterReportData = filterReportDomainService.getFilterReportData(filter.getFilterId());
                    final var decodedTuples = filterQueryExecutor.getFieldsValues(new FilterRequestData(filterReportData, filter.getParameters()));

                    // Убираем все поля кроме CODE для VALUE_LIST фильтров и избавляемся от дублей
                    if (filterReportData.filterType().equals(FilterTypeEnum.VALUE_LIST)) {
                        final var codeFieldId = filterReportData.getFieldByLevelAndType(1, FilterFieldTypeEnum.CODE_FIELD).fieldId();
                        var cleanTuples = Collections.singletonList(new Tuple(decodedTuples
                                .stream()
                                .flatMap(tuple -> tuple.getValues().stream())
                                .filter(field -> field.getFieldId().equals(codeFieldId))
                                .distinct()
                                .toList()));
                        decodedTuples.clear();
                        decodedTuples.addAll(cleanTuples);
                    }

                    filter.getParameters().clear();
                    filter.setParameters(decodedTuples);
                });
    }

    public FolderResponse getFavReports() {

        final var currentUser = userDomainService.getCurrentUser();
        final var favReports = reportDomainService.getFavReports(currentUser);
        favReports.getReports().forEach(report -> report.setFavorite(true));
        return favReports;
    }

    public void deleteReportToFavorites(ReportIdRequest request) {
        final var currentUser = userDomainService.getCurrentUser();
        reportDomainService.deleteReportToFavorites(currentUser, request);
    }

    public void changeReportParentFolder(ChangeParentFolderRequest request) {
        permissionCheckerSystem.checkPermissionsOnAllFolders(request, reportDomainService::getFolderIds, folderPermissionsDomainService::getExcelTemplateFolderPermissionsForRoles);
        reportDomainService.changeFilterInstanceParentFolder(request);
    }

    public void copyReport(ChangeParentFolderRequest request) {
        permissionCheckerSystem.checkPermissionsOnAllFolders(request, null, folderPermissionsDomainService::getReportFolderPermissionsForRoles);
        reportDomainService.copyReport(request, userDomainService.getCurrentUser());
    }

    public List<ReportFolderResponse> copyReportFolder(CopyFolderRequest request) {
        var roles = userDomainService.getUserRoles(userDomainService.getCurrentUser().getName(), null).stream().map(RoleView::getId).toList();

        folderPermissionsDomainService.getReportFolderPermissionsForRoles(request.getFolderIds(), roles)
                .forEach(f -> {
                    if (f.getAuthority() == FolderAuthorityEnum.NONE)
                        throw new InvalidParametersException(String.format("Copy for folder is not available with id %s: Permission denied", f.getFolderId()));

                });

        folderPermissionsDomainService.getReportFolderPermissionsForRoles(Collections.singletonList(request.getDestFolderId()), roles)
                .forEach(f -> {
                    if (f.getAuthority() != FolderAuthorityEnum.WRITE)
                        throw new InvalidParametersException("Dest folder is not available: Permission denied" + f.getFolderId());
                });

        var newFolders = reportDomainService.copyReportFolder(request, userDomainService.getCurrentUser());
        return newFolders.stream().map(f -> reportDomainService.getFolder(userDomainService.getCurrentUser().getId(),f)).toList();
    }
}
