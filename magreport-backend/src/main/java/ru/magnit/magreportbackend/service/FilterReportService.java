package ru.magnit.magreportbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTaskStatusEnum;
import ru.magnit.magreportbackend.domain.user.UserRoleTypeEnum;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.dto.request.filterinstance.ChildNodesRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.ListValuesCheckRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.ListValuesRequest;
import ru.magnit.magreportbackend.dto.request.filterreport.FilterAddRequest;
import ru.magnit.magreportbackend.dto.request.filterreport.FilterGroupAddRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportRequest;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterGroupResponse;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportChildNodesResponse;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportValuesCheckResponse;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportValuesResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.service.domain.DataSetDomainService;
import ru.magnit.magreportbackend.service.domain.FilterInstanceDomainService;
import ru.magnit.magreportbackend.service.domain.FilterReportDomainService;
import ru.magnit.magreportbackend.service.domain.MailTextDomainService;
import ru.magnit.magreportbackend.service.domain.ReportDomainService;
import ru.magnit.magreportbackend.service.domain.ScheduleTaskDomainService;
import ru.magnit.magreportbackend.service.domain.SecurityFilterDomainService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.magnit.magreportbackend.domain.dataset.DataSetTypeEnum.PROCEDURE;

@Service
@RequiredArgsConstructor
public class FilterReportService {

    private final FilterReportDomainService domainService;
    private final FilterInstanceDomainService filterInstanceDomainService;
    private final UserDomainService userDomainService;
    private final SecurityFilterDomainService securityFilterDomainService;
    private final ReportDomainService reportDomainService;
    private final DataSetDomainService dataSetDomainService;
    private final ScheduleTaskDomainService scheduleTaskDomainService;
    private final MailTextDomainService mailTextDomainService;

    public FilterGroupResponse addFilters(FilterGroupAddRequest request) {

        final var currentUser = userDomainService.getCurrentUser();
        request = filterInstanceDomainService.addMissingFields(request);

        var typeDataSetID = dataSetDomainService
                .getDataSet(reportDomainService.getReport(request.getReportId()).getDataSetId())
                .getTypeId();

        if (PROCEDURE.equalsIsLong(typeDataSetID))
            checkCodesUnique(request);


        domainService.addFilters(currentUser, request);

        scheduleTaskDomainService.findScheduleTaskByReport(request.getReportId())
                .forEach(task -> {
                    if(!task.getStatus().equals(ScheduleTaskStatusEnum.CHANGED)) {
                        scheduleTaskDomainService.setStatusScheduleTask(task.getId(), ScheduleTaskStatusEnum.CHANGED);
                        mailTextDomainService.sendScheduleMailChanged(task);
                    }
                });

        return domainService.getFilters(request.getReportId());
    }

    public FilterGroupResponse getFilters(ReportRequest request) {

        return domainService.getFilters(request.getId());
    }

    public FilterReportValuesResponse getFilterReportValues(ListValuesRequest request) {

        return domainService.getFilterReportValues(request);
    }

    public FilterReportChildNodesResponse getChildNodes(ChildNodesRequest request) {

        final var currentUserRoles = userDomainService.getCurrentUserRoles(UserRoleTypeEnum.MANUAL);
        final var dataSetId = domainService.getFilterReportDataSetId(request.getFilterId());
        final var effectiveSettings = securityFilterDomainService.getEffectiveSettings(dataSetId, currentUserRoles.stream().map(RoleView::getId).collect(Collectors.toSet()));


        return domainService.getChildNodes(request, effectiveSettings);
    }

    public FilterReportValuesCheckResponse checkFilterReportValues(ListValuesCheckRequest request) {
        return domainService.checkFilterReportValues(request);
    }

    private void checkCodesUnique(FilterGroupAddRequest request) {
        final var codes = getGroups(request, new ArrayList<>()).stream()
                .flatMap(g -> g.getFilters().stream())
                .map(FilterAddRequest::getCode)
                .toList();

        if (codes.contains("")) {
            throw new InvalidParametersException("FilterReport with empty Code");
        }

        if (codes.size() != codes.stream().distinct().count()) {
            throw new InvalidParametersException("FilterReport with not unique Code");
        }
    }

    private List<FilterGroupAddRequest> getGroups(FilterGroupAddRequest request, List<FilterGroupAddRequest> groups) {
        if (request == null) return groups;
        groups.add(request);
        groups.addAll(request.getChildGroups().stream().flatMap(group -> getGroups(group, new ArrayList<>()).stream()).toList());
        return groups;
    }
}
