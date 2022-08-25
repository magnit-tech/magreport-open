package ru.magnit.magreportbackend.service.jobengine;

import ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum;
import ru.magnit.magreportbackend.dto.inner.jobengine.ReportReaderData;

public interface ReportReaderFactory {

    ReportReader getReader(ReportReaderData readerData);

    QueryBuilder getQueryBuilder(DataSourceTypeEnum dataSourceType);
}
