package ru.magnit.magreportbackend.service.domain;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportFilterGroupData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportFilterLevelData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobFilterData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobTupleFieldData;
import ru.magnit.magreportbackend.service.jobengine.filter.FilterQueryExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ExportDataInExternalTableDomainService {

    private final FilterQueryExecutor filterQueryService;


    public void exportData(ReportJobData reportJobData) {
        var reportFilterGroupData = reportJobData.reportData().filterGroup();
        if (reportFilterGroupData == null)
            return;

        List<String> queryInserts = new ArrayList<>();
        Set<FieldTuple> controlField = new HashSet<>();
        var idTuple = new AtomicLong(0L);
        var schema = reportJobData.reportData().schemaName();
        var jobFilters = new HashMap<Long, ReportJobFilterData>();

        insertsSecurityFiltersValues(reportJobData.id(), schema, reportJobData.securityFilterParameters(), queryInserts, idTuple);
        reportJobData.parameters().forEach(param -> jobFilters.put(param.filterId(), param));

        getReportFilterGroup(reportFilterGroupData, reportJobData.id(), jobFilters, queryInserts, schema, idTuple, controlField);
        var query = String.join("", queryInserts);

        filterQueryService.executeSql(reportJobData.dataSource(), query);
    }

    private void getReportFilterGroup(ReportFilterGroupData group, Long idJob, HashMap<Long, ReportJobFilterData> jobFilters, List<String> queryInserts, String schema, AtomicLong idTuple, Set<FieldTuple> controlField) {

        queryInserts.add(getInsertReportFilterGroup(idJob, schema, group.code(), group.parentCode(), group.operationType().name()));

        var filters = group.filters();
        filters.forEach(filter -> {

            var jobFilter = jobFilters.get(filter.filterId());
            if (jobFilter == null) return;

            var fieldsFilter = filter.fields().stream().collect(Collectors.toMap(ReportFilterLevelData::idFieldId, f -> f));

            queryInserts.add(getInsertReportFilter(idJob, schema, filter.code(), group.code(), jobFilter.filterType(), jobFilter.operationType()));

            jobFilter.fieldValues().forEach(tupleData -> {
                queryInserts.add(getInsertReportFilterTuple(idJob, schema, idTuple.get(), filter.code()));
                tupleData.fieldValues().forEach(fieldData -> {
                    if (controlField.add(new FieldTuple(fieldData.fieldId(), idTuple.get()))) {
                        var field = fieldsFilter.get(fieldData.fieldId());
                        queryInserts.add(getInsertReportFilterField(idJob, schema, fieldData.fieldId(), idTuple.get(), field.filterCodeFieldName(),  field.filterFieldType(), fieldData.level()));
                        queryInserts.add(getInsertReportFilterFieldValueByTypeField(idJob, schema, idTuple.get(),  field.filterFieldType(), fieldData));
                    }
                });
                idTuple.getAndIncrement();
            });

        });

        if (!group.groups().isEmpty())
            group.groups().forEach(child -> getReportFilterGroup(child, idJob, jobFilters, queryInserts, schema, idTuple, controlField));

    }

    private void insertsSecurityFiltersValues(Long idJob, String schema, List<ReportJobFilterData> paramSecurityFiltersList, List<String> queryInserts, AtomicLong idTuple) {

        queryInserts.add(getInsertReportFilterGroup(idJob, schema, "SECURITY_FILTER", null, "AND"));

        paramSecurityFiltersList.forEach(filter -> {
            queryInserts.add(getInsertReportFilter(idJob, schema, "SF_" + filter.code(), "SECURITY_FILTER", filter.filterType(), filter.operationType()));
            filter.fieldValues().forEach(tupleData -> {
                queryInserts.add(getInsertReportFilterTuple(idJob, schema, idTuple.get(), "SF_" + filter.code()));
                tupleData.fieldValues().forEach(fieldData -> {
                    queryInserts.add(getInsertReportFilterField(idJob, schema, fieldData.fieldId(), idTuple.get(), fieldData.fieldName(), fieldData.fieldDataType(), fieldData.level()));
                    queryInserts.add(getInsertReportFilterFieldValueByTypeField(idJob, schema, idTuple.get(), fieldData.fieldDataType(), fieldData));
                });
                idTuple.getAndIncrement();
            });
        });

    }

    private String getInsertReportFilterGroup(Long idJob, String schema, String groupName, String parentGroupName, String operationType) {

        groupName = groupName == null ? null : "'" + groupName + "'";
        parentGroupName = parentGroupName == null ? null : "'" + parentGroupName + "'";
        operationType = operationType == null ? null : "'" + operationType + "'";


        return String.format(
                "INSERT INTO %s.T_REPORT_FILTER_GROUP (JOB_ID,FILTER_GROUP_NAME,PARENT_FILTER_GROUP_NAME, GROUP_OPERATION) " +
                        "VALUES (%s, %s, %s, %s); ",
                schema, idJob, groupName, parentGroupName, operationType);
    }

    private String getInsertReportFilter(Long idJob, String schema, String filterCode, String groupCode, FilterTypeEnum typeFilter, FilterOperationTypeEnum operationTypeFilter) {
        var filter = filterCode == null ? null : "'" + filterCode + "'";
        var group = groupCode == null ? null : "'" + groupCode + "'";
        var type = typeFilter == null ? null : "'" + typeFilter + "'";
        var operation = operationTypeFilter == null ? null : "'" + operationTypeFilter + "'";

        return String.format(
                "INSERT INTO %s.T_REPORT_FILTER (JOB_ID,REPORT_FILTER_NAME,FILTER_GROUP_NAME,FILTER_TYPE,OPERATION_TYPE) " +
                        "VALUES (%s, %s, %s, %s, %s);",
                schema, idJob, filter, group, type, operation);
    }

    private String getInsertReportFilterTuple(Long idJob, String schema, Long idTuple, String filterCode) {
        var filter = filterCode == null ? null : "'" + filterCode + "'";
        return String.format(
                "INSERT INTO %s.T_REPORT_FILTER_TUPLE (JOB_ID,TUPLE_ID,REPORT_FILTER_NAME) " +
                        "VALUES (%s, %s, %s);",
                schema, idJob, idTuple, filter);
    }

    private String getInsertReportFilterField(Long idJob, String schema, Long fieldId, Long idTuple, String fieldName, DataTypeEnum type, Long level) {
        var field = fieldName == null ? null : "'" + fieldName + "'";
        var typeField = type == null ? null : "'" + type + "'";
        return String.format(
                "INSERT INTO %s.T_REPORT_FILTER_FIELD (JOB_ID,REPORT_FILTER_FIELD_ID,TUPLE_ID,FIELD_NAME,DATA_TYPE_FIELD,LEVEL) " +
                        "VALUES (%s, %s, %s, %s, %s, %s);",
                schema, idJob, fieldId, idTuple, field, typeField, level);
    }

    private String getInsertReportFilterFieldValue(Long idJob, String schema, Long idTuple, Long fieldID, String fieldValChar, String fieldValData, String fieldValInteger, String fieldValDouble) {
        fieldValChar = fieldValChar == null ? null : "'" + fieldValChar + "'";
        fieldValData = fieldValData == null ? null : "'" + fieldValData + "'";

        return String.format(
                "INSERT INTO %s.T_REPORT_FILTER_FIELD_VALUE (JOB_ID,REPORT_FILTER_FIELD_ID,TUPLE_ID,VARCHAR_VALUE,DATE_VALUE,INTEGER_VALUE,DOUBLE_VALUE) " +
                        "VALUES (%s, %s, %s, %s, %s, %s, %s);",
                schema, idJob, fieldID, idTuple, fieldValChar, fieldValData, fieldValInteger, fieldValDouble);

    }

    private String getInsertReportFilterFieldValueByTypeField(Long idJob, String schema, Long idTuple, DataTypeEnum type, ReportJobTupleFieldData field) {

        return switch (type) {
            case STRING, TIMESTAMP -> getInsertReportFilterFieldValue(idJob, schema, idTuple, field.fieldId(), field.value(), null, null, null);
            case DATE -> getInsertReportFilterFieldValue(idJob, schema, idTuple, field.fieldId(), null, field.value(), null, null);
            case INTEGER -> getInsertReportFilterFieldValue(idJob, schema, idTuple, field.fieldId(), null, null, field.value(), null);
            case DOUBLE -> getInsertReportFilterFieldValue(idJob, schema, idTuple, field.fieldId(), null, null, null, field.value());
        };
    }

    private record FieldTuple(Long fieldId, Long tupleId) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            var fieldTuple = (FieldTuple) o;
            return Objects.equals(fieldId, fieldTuple.fieldId) && Objects.equals(tupleId, fieldTuple.tupleId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fieldId, tupleId);
        }
    }
}
