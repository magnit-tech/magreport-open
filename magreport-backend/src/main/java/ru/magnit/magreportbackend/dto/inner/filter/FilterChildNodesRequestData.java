package ru.magnit.magreportbackend.dto.inner.filter;

import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobFilterData;

import java.util.List;
import java.util.Map;

public record FilterChildNodesRequestData(
        DataSourceData dataSource,
        String schemaName,
        String tableName,
        Long rootFieldId,
        Long responseFieldId,
        String idFieldName,
        String nameFieldName,
        long level,
        Map<String, String> pathValues,
        List<ReportJobFilterData> securitySettings
        ) {}