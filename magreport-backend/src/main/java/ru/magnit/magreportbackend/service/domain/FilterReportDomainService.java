package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportGroup;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.inner.filter.FilterData;
import ru.magnit.magreportbackend.dto.inner.filter.FilterRequestData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportFilterData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportFilterGroupData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportFilterLevelData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobFilterData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobTupleData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobTupleFieldData;
import ru.magnit.magreportbackend.dto.request.filterinstance.ChildNodesRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.ListValuesCheckRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.ListValuesRequest;
import ru.magnit.magreportbackend.dto.request.filterreport.FilterGroupAddRequest;
import ru.magnit.magreportbackend.dto.request.reportjob.ReportJobFilterRequest;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterNodeResponse;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterGroupResponse;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportChildNodesResponse;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportResponse;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportValuesCheckResponse;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportValuesResponse;
import ru.magnit.magreportbackend.dto.tuple.Tuple;
import ru.magnit.magreportbackend.dto.tuple.TupleValue;
import ru.magnit.magreportbackend.mapper.filter.FilterChildNodesRequestDataMerger;
import ru.magnit.magreportbackend.mapper.filterreport.FilterDataFRMapper;
import ru.magnit.magreportbackend.mapper.filterreport.FilterGroupResponseMapper;
import ru.magnit.magreportbackend.mapper.filterreport.FilterReportGroupMapper;
import ru.magnit.magreportbackend.mapper.filterreport.FilterReportQueryDataMerger;
import ru.magnit.magreportbackend.mapper.filterreport.FilterReportResponseMapper;
import ru.magnit.magreportbackend.repository.FilterReportGroupRepository;
import ru.magnit.magreportbackend.repository.FilterReportRepository;
import ru.magnit.magreportbackend.service.jobengine.filter.FilterQueryExecutor;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilterReportDomainService {

    private final FilterReportGroupRepository filterGroupRepository;
    private final FilterReportRepository filterReportRepository;
    private final FilterQueryExecutor queryExecutor;

    private final FilterReportGroupMapper filterReportGroupMapper;
    private final FilterGroupResponseMapper filterGroupResponseMapper;
    private final FilterReportResponseMapper filterReportResponseMapper;
    private final FilterReportQueryDataMerger filterReportQueryDataMerger;
    private final FilterDataFRMapper filterDataFRMapper;
    private final FilterChildNodesRequestDataMerger filterChildNodesRequestDataMerger;

    @Transactional
    public void addFilters(UserView currentUser, FilterGroupAddRequest request) {
        if (request == null || request.getReportId() == null) return;

        filterGroupRepository.deleteByReportId(request.getReportId());

        var filterReportGroup = filterReportGroupMapper.from(request);
        final var user = new User(currentUser.getId());
        final var filterReportGroups = unWindGroups(filterReportGroup, new LinkedList<>());
        filterReportGroups.forEach(group -> group.getFilterReports().forEach(filter -> filter.setUser(user)));

        filterGroupRepository.save(filterReportGroup);
    }

    private List<FilterReportGroup> unWindGroups(FilterReportGroup rootGroup, List<FilterReportGroup> result) {
        result.add(rootGroup);
        if (rootGroup.getChildGroups().isEmpty())
            return result;
        else
            return rootGroup
                    .getChildGroups()
                    .stream()
                    .flatMap(group -> unWindGroups(group, result).stream())
                    .toList();
    }

    @Transactional
    public FilterGroupResponse getFilters(Long reportId) {
        var filterReportGroup = filterGroupRepository.getByReportIdAndParentGroupIsNull(reportId);
        return filterGroupResponseMapper.from(filterReportGroup);
    }

    @Transactional
    public void removeFilters(Long reportId) {

        filterGroupRepository.deleteByReportId(reportId);
    }

    @Transactional
    public FilterReportValuesResponse getFilterReportValues(ListValuesRequest request) {

        final var filterReport = filterReportRepository.getReferenceById(request.getFilterId());

        final var requestData = filterReportQueryDataMerger.merge(filterReport, request);
        final var tuples = queryExecutor.getFilterInstanceValuesQuery(requestData);

        return new FilterReportValuesResponse(filterReportResponseMapper.from(filterReport), tuples);
    }

    @Transactional
    public FilterReportChildNodesResponse getChildNodes(ChildNodesRequest request, List<ReportJobFilterData> effectiveSettings) {

        final var filterReport = filterReportRepository.getReferenceById(request.getFilterId());
        final var requestData = filterChildNodesRequestDataMerger.merge(filterDataFRMapper.from(filterReport), request, effectiveSettings);
        final var childNodes = queryExecutor.getFilterInstanceChildNodes(requestData);
        final var rootNode = new FilterNodeResponse(
                requestData.rootFieldId(),
                requestData.level(),
                null,
                null,
                childNodes);

        return new FilterReportChildNodesResponse(filterReportResponseMapper.from(filterReport), requestData.responseFieldId(), rootNode);
    }

    @Transactional
    public void decodeFieldsValues(ReportJobData jobData) {
        var decodedParameters = new LinkedList<ReportJobFilterData>();

        decodeListValueCodeFields(jobData);

        jobData.parameters()
                .forEach(filter -> decodedParameters.add(new ReportJobFilterData(
                        filter.filterId(),
                        filter.filterType(),
                        filter.operationType(),
                        filter.code(),
                        decodeTuples(jobData.reportData(), filter.fieldValues()))));

        jobData.parameters().clear();
        jobData.parameters().addAll(decodedParameters);
    }

    private void decodeListValueCodeFields(ReportJobData jobData) {

        var emptyFilters = new HashSet<ReportJobFilterData>();

        jobData.parameters()
                .stream()
                .filter(filter -> filter.filterType() == FilterTypeEnum.VALUE_LIST)
                .forEach(filter -> {
                    var filterReportData = getFilterReportData(filter.filterId());
                    var filterRequestData = new FilterRequestData(
                            filterReportData,
                            getTuplesFromParameters(filter.fieldValues()));
                    var fieldsValues = queryExecutor.getFieldsValues(filterRequestData);
                    var codeFieldId = filterReportData.getFieldByLevelAndType(1, FilterFieldTypeEnum.CODE_FIELD).fieldId();
                    if (fieldsValues.isEmpty()) {
                        emptyFilters.add(filter);
                    } else {
                        filter.fieldValues().clear();
                        filter.fieldValues().addAll(fieldsValues
                                .stream()
                                .map(tuple -> new ReportJobTupleData(tuple.getValues()
                                        .stream()
                                        .filter(value -> !value.getFieldId().equals(codeFieldId))
                                        .map(value -> new ReportJobTupleFieldData(codeFieldId, 1L, null, null, value.getValue()))
                                        .toList()))
                                .toList());
                    }
                });
        jobData.parameters().removeIf(emptyFilters::contains);
    }

    private List<Tuple> getTuplesFromParameters(List<ReportJobTupleData> parametersTuples) {
        return parametersTuples
                .stream()
                .map(tuple -> new Tuple(tuple.fieldValues()
                        .stream()
                        .map(fieldValue -> new TupleValue(fieldValue.fieldId(), fieldValue.value()))
                        .toList()))
                .toList();
    }

    @Transactional
    public FilterData getFilterReportData(Long filterId) {
        final var filterReport = filterReportRepository.getReferenceById(filterId);
        return filterDataFRMapper.from(filterReport);
    }

    @Transactional
    public Long getFilterReportDataSetId(Long filterId) {

        final var filterReport = filterReportRepository.getReferenceById(filterId);

        return filterReport.getFilterInstance().getDataSet().getId();
    }

    @Transactional
    public List<Tuple> getCleanedValueListValues(ReportJobFilterRequest paramValues) {
        final var filterData = getFilterReportData(paramValues.getFilterId());
        final var requestData = new FilterRequestData(filterData, paramValues.getParameters());
        return queryExecutor.getFieldsValues(requestData);
    }

    @Transactional
    public List<FilterReportResponse> checkFilterReportForInstance(Long filterInstanceId) {
        var result = filterReportRepository.findFilterReportByFilterInstanceId(filterInstanceId);
        return result.stream().map(filterReportResponseMapper::from).toList();
    }

    @Transactional
    public FilterReportValuesCheckResponse checkFilterReportValues(ListValuesCheckRequest request) {
        final var filterReport = filterReportRepository.getReferenceById(request.getFilterId());

        final var tuples = queryExecutor.getFieldsValues(new FilterRequestData(filterDataFRMapper.from(filterReport), Collections.singletonList(new Tuple(request.getValues()))));
        final var tupleValues = request.getValues()
                .stream()
                .map(TupleValue::getValue)
                .filter(value -> tuples.stream().flatMap(tuple -> tuple.getValues().stream()).noneMatch(val -> val.getValue().equals(value)))
                .toList();

        return new FilterReportValuesCheckResponse(filterReportResponseMapper.from(filterReport), tupleValues);
    }

    private List<ReportJobTupleData> decodeTuples(ReportData reportData, List<ReportJobTupleData> fieldValues) {
        var newTuples = new LinkedList<ReportJobTupleData>();

        fieldValues.forEach(tuple -> newTuples.add(
                new ReportJobTupleData(
                        decodeFieldValues(reportData, tuple.fieldValues()))));

        return newTuples;
    }

    private List<ReportJobTupleFieldData> decodeFieldValues(ReportData reportData, List<ReportJobTupleFieldData> tuple) {
        var result = new LinkedList<ReportJobTupleFieldData>();

        var filters = unWind(reportData.filterGroup(), new LinkedList<>());

        tuple.forEach(tupleData -> result.add(
                new ReportJobTupleFieldData(
                        filters.stream().flatMap(filter -> filter.fields().stream()).filter(field -> tupleData.fieldId().equals(field.codeFieldId())).map(ReportFilterLevelData::idFieldId).findFirst().orElse(-1L),
                        tupleData.level(),
                        filters.stream().flatMap(filter -> filter.fields().stream()).filter(field -> tupleData.fieldId().equals(field.codeFieldId())).map(ReportFilterLevelData::reportFieldName).findFirst().orElse(null),
                        filters.stream().flatMap(filter -> filter.fields().stream()).filter(field -> tupleData.fieldId().equals(field.codeFieldId())).map(ReportFilterLevelData::reportFieldType).findFirst().orElse(null),
                        tupleData.value())));

        return result;
    }

    private List<ReportFilterData> unWind(ReportFilterGroupData reportFilterGroupData, List<ReportFilterData> reportFilterData) {

        if (reportFilterGroupData == null) return reportFilterData;
        reportFilterData.addAll(reportFilterGroupData.filters());
        reportFilterData.addAll(reportFilterGroupData.groups().stream().flatMap(group -> unWind(group, reportFilterData).stream()).toList());
        return reportFilterData;
    }

    @Transactional
    public void updateExpandFields(Long filterInstanceId) {

        var filters = filterReportRepository.findFilterReportByFilterInstanceId(filterInstanceId);
        filters.forEach(filter -> filter.getFields()
                .forEach(field -> {
                    var expand = field.getFilterInstanceField().getExpand();
                    field.setExpand(expand);
                })
        );
        filterReportRepository.saveAll(filters);
    }
}
