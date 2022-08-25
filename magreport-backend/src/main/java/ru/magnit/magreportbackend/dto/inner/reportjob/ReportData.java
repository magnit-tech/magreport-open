package ru.magnit.magreportbackend.dto.inner.reportjob;

import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.exception.InvalidParametersException;

import java.util.List;

public record ReportData(

        long id,
        String name,
        String description,
        String schemaName,
        String tableName,
        List<ReportFieldData> fields,
        ReportFilterGroupData filterGroup
) {

    public List<ReportFieldData> getVisibleFields() {
        return fields.stream().filter(ReportFieldData::visible).toList();
    }

    public DataTypeEnum getTypeField(Long fieldId){
        var result = fields
                .stream()
                .filter(field  -> field.id() == fieldId)
                .map(ReportFieldData::dataType)
                .findFirst();

        return result.orElseThrow(() -> new InvalidParametersException(""));
    }
}
