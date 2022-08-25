package ru.magnit.magreportbackend.dto.inner.filter;

import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;
import ru.magnit.magreportbackend.dto.request.filterinstance.LikenessType;

public record FilterValueListRequestData(
        DataSourceData dataSource,
        String schemaName,
        String tableName,
        String filterFieldName,
        DataTypeEnum filterFieldType,
        Long idFieldId,
        String idFieldName,
        Long codeFieldId,
        String codeFieldName,
        Long nameFieldId,
        String nameFieldName,
        boolean isCaseSensitive,
        LikenessType likenessType,
        long maxCount,
        String searchValue
) {}
