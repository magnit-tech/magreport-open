package ru.magnit.magreportbackend.dto.inner.reportjob;

import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public record ReportJobFilterData(
        Long filterId,
        FilterTypeEnum filterType,
        FilterOperationTypeEnum operationType,
        String code,
        List<ReportJobTupleData> fieldValues
) {
    public String getValue() {
        final var value = fieldValues.get(0).fieldValues().get(0);
        return value.fieldDataType().quote(value.value());
    }

    @SuppressWarnings("unused")
    public String getValuesForSqlIn() {
        return fieldValues
                .stream()
                .flatMap(o -> o.fieldValues().stream())
                .map(o -> o.fieldDataType().quote(o.value())).collect(Collectors.joining(", "));
    }

    @SuppressWarnings("unused")
    public String getImpalaSqlBetween() {
        final var fieldFrom = fieldValues.get(0).fieldValues().get(0);
        final var fieldTo = fieldValues.get(0).fieldValues().get(1);

        return "BETWEEN " +
                fieldFrom.fieldDataType().quote(fieldFrom.fieldDataType() == DataTypeEnum.INTEGER ? fieldFrom.value().replace("-", "") : fieldFrom.value()) +
                " AND " +
                fieldTo.fieldDataType().quote(fieldTo.fieldDataType() == DataTypeEnum.INTEGER ? fieldTo.value().replace("-", "") : fieldTo.value());
    }

    public String getImpalaSqlM2M() {
        return fieldValues
                .stream()
                .map(tuple -> tuple.fieldValues()
                        .stream()
                        .map(value -> value.fieldName() + " = " + value.fieldDataType().quote(value.value()))
                        .collect(Collectors.joining(" AND ", "(", ")")))
                .collect(Collectors.joining(" OR "));
    }

    public String getImpalaSqlStrict() {
        var fieldParameters = fieldValues
                .stream()
                .flatMap(tuple -> tuple.fieldValues()
                        .stream()
                        .sorted(Comparator.comparingLong(ReportJobTupleFieldData::level).reversed())
                        .limit(1))
                .collect(Collectors.groupingBy(ReportJobTupleFieldData::fieldName));

        return fieldParameters.entrySet()
                .stream()
                .map(field -> field.getKey() + " IN " + field.getValue()
                        .stream()
                        .map(value -> value.fieldDataType().quote(value.value()))
                        .collect(Collectors.joining(", ", "(", ")")))
                .collect(Collectors.joining(" OR "));
    }
}
