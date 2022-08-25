package ru.magnit.magreportbackend.dto.inner.filter;

import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;

public record FilterFieldData(
        long fieldId,
        long level,
        String name,
        String description,
        String fieldName,
        DataTypeEnum dataType,
        FilterFieldTypeEnum fieldType
) {}