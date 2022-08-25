package ru.magnit.magreportbackend.dto.inner.filter;

import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;
import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;
import ru.magnit.magreportbackend.dto.tuple.TupleValue;
import ru.magnit.magreportbackend.exception.InvalidParametersException;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record FilterData(
   DataSourceData dataSource,
   long filterId,
   FilterTypeEnum filterType,
   String schemaName,
   String tableName,
   String name,
   String code,
   String description,
   List<FilterFieldData> fields
) {

    public FilterFieldData getRootField(List<TupleValue> pathNodes) {

        var fieldPath = getIdSet(pathNodes);

        return fields
                .stream()
                .filter(field -> fieldPath.contains(field.fieldId()))
                .max(Comparator.comparingLong(FilterFieldData::level))
                .orElseThrow(() -> new InvalidParametersException("Can't find root node in path:" + fieldPath));
    }

    private Set<Long> getIdSet(List<TupleValue> pathNodes) {

        return pathNodes.stream().map(TupleValue::getFieldId).collect(Collectors.toSet());
    }

    public FilterFieldData getFieldByLevelAndType(long level, FilterFieldTypeEnum fieldType) {

        return fields
                .stream()
                .filter(field -> field.level() == level)
                .filter(field -> field.fieldType() == fieldType)
                .findFirst()
                .orElseThrow(()-> new InvalidParametersException("Field with level:" + level + " and type:" + fieldType + " was not found."))
                ;
    }

    public Map<String, String> getPathValues(List<TupleValue> pathNodes) {

        var result = new LinkedHashMap<String, String>();

        pathNodes.forEach(node -> result.put(getFieldNameByFieldId(node.getFieldId()), getValue(node)));

        return result;
    }

    public String getValue(TupleValue node) {

        final var field = getFieldById(node.getFieldId());

        return field.dataType().quote(node.getValue());
    }

    public String getFieldNameByFieldId(Long fieldId) {

        return getFieldById(fieldId).fieldName();
    }

    public FilterFieldData getFieldById(Long fieldId) {

        return fields
                .stream()
                .filter(field -> field.fieldId() == fieldId)
                .findFirst()
                .orElseThrow(()-> new InvalidParametersException("Field with id:" + fieldId + " was not found."));
    }
}
