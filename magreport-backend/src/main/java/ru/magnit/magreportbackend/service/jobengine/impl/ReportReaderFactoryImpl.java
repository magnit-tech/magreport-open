package ru.magnit.magreportbackend.service.jobengine.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum;
import ru.magnit.magreportbackend.dto.inner.jobengine.ReportReaderData;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.service.dao.ConnectionPoolManager;
import ru.magnit.magreportbackend.service.jobengine.QueryBuilder;
import ru.magnit.magreportbackend.service.jobengine.ReportReader;
import ru.magnit.magreportbackend.service.jobengine.ReportReaderFactory;

import java.util.List;
import java.util.Map;

import static ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum.DB2;
import static ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum.H2;
import static ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum.IMPALA;
import static ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum.MSSQL;
import static ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum.ORACLE;
import static ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum.POSTGRESQL;
import static ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum.TERADATA;

@Service
@RequiredArgsConstructor
public class ReportReaderFactoryImpl implements ReportReaderFactory {

    private final List<QueryBuilder> queryBuilders;
    private final ConnectionPoolManager poolManager;

    @Value("${magreport.jobengine.max-rows}")
    private Long maxRows;

    private static final Map<DataSourceTypeEnum, Class<?>> builderTypes = Map.ofEntries(
            Map.entry(H2, H2QueryBuilder.class),
            Map.entry(TERADATA, TeradataQueryBuilder.class),
            Map.entry(IMPALA, ImpalaQueryBuilder.class),
            Map.entry(ORACLE, OracleQueryBuilder.class),
            Map.entry(MSSQL, MsSqlQueryBuilder.class),
            Map.entry(POSTGRESQL, PostgreSqlQueryBuilder.class),
            Map.entry(DB2, PostgreSqlQueryBuilder.class)
    );

    @Override
    public ReportReader getReader(ReportReaderData readerData) {

        return new ReportReaderImpl(readerData, getQueryBuilder(readerData.dataSource().type()), poolManager, maxRows);
    }

    @Override
    public QueryBuilder getQueryBuilder(DataSourceTypeEnum dataSourceType) {

        return queryBuilders
                .stream()
                .filter(o -> o.getClass().equals(builderTypes.get(dataSourceType)))
                .findFirst()
                .orElseThrow(() -> new InvalidParametersException("QueryBuilder not found for database type '" + dataSourceType + "'"));
    }
}