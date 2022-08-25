package ru.magnit.magreportbackend.dto.inner.reportjob;

import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;

public record ReportFieldData(

        long id,
        int ordinal,
        boolean visible,
        DataTypeEnum dataType,
        String columnName,
        String name,
        String description
) {
}
