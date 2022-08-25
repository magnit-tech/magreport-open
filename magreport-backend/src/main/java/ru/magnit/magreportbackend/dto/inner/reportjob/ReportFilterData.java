package ru.magnit.magreportbackend.dto.inner.reportjob;

import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;

import java.util.List;

public record ReportFilterData(
        long filterId,
        DataSourceData dataSource,
        Long dataSetId,
        String code,
        String schemaName,
        String tableName,
        List<ReportFilterLevelData> fields
) {}