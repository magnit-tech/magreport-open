package ru.magnit.magreportbackend.dto.inner.reportjob;

import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;

public record ReportFilterLevelData(
        long idFieldId,
        long codeFieldId,
        DataTypeEnum reportFieldType,
        String reportFieldName,
        String filterIdFieldName,
        String filterCodeFieldName,
        DataTypeEnum filterFieldType
) {}