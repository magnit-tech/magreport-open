package ru.magnit.magreportbackend.service.jobengine.filter.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum;
import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;
import ru.magnit.magreportbackend.dto.inner.filter.FilterChildNodesRequestData;
import ru.magnit.magreportbackend.dto.inner.filter.FilterRequestData;
import ru.magnit.magreportbackend.dto.inner.filter.FilterValueListRequestData;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterNodeResponse;
import ru.magnit.magreportbackend.dto.tuple.Tuple;
import ru.magnit.magreportbackend.dto.tuple.TupleValue;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.exception.QueryExecutionException;
import ru.magnit.magreportbackend.service.dao.ConnectionPoolManager;
import ru.magnit.magreportbackend.service.jobengine.filter.FilterQueryBuilder;
import ru.magnit.magreportbackend.service.jobengine.filter.FilterQueryExecutor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum.DB2;
import static ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum.H2;
import static ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum.IMPALA;
import static ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum.ORACLE;
import static ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum.POSTGRESQL;
import static ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum.TERADATA;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilterQueryExecutorImpl implements FilterQueryExecutor {

    private static final String QUERY = "Query:\n";
    private static final String QUERY_FAILED = "Query failed:\n";
    private final ConnectionPoolManager poolManager;
    private final List<FilterQueryBuilder> queryBuilders;

    private static final Map<DataSourceTypeEnum, Class<?>> builderTypes = Map.ofEntries(
            Map.entry(H2, H2FilterQueryBuilder.class),
            Map.entry(IMPALA, ImpalaFilterQueryBuilder.class),
            Map.entry(TERADATA, TeradataFilterQueryBuilder.class),
            Map.entry(ORACLE, OracleFilterQueryBuilder.class),
            Map.entry(POSTGRESQL, PostgreSqlFilterQueryBuilder.class),
            Map.entry(DB2, PostgreSqlFilterQueryBuilder.class)
    );

    @Override
    public List<Tuple> getFilterInstanceValuesQuery(FilterValueListRequestData requestData) {
        final var queryBuilder = getQueryBuilder(requestData.dataSource().type());
        final var filterInstanceValuesQuery = queryBuilder.getFilterValuesQuery(requestData);

        log.debug(QUERY + filterInstanceValuesQuery);
        List<Tuple> tuples = new LinkedList<>();

        try (
                var connection = poolManager.getConnection(requestData.dataSource());
                var statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                var resultSet = statement.executeQuery(filterInstanceValuesQuery)
        ) {
            while (resultSet.next()) {
                var tuple = new Tuple();
                tuple.getValues().add(new TupleValue(requestData.idFieldId(), resultSet.getString(1)));
                tuple.getValues().add(new TupleValue(requestData.codeFieldId(), resultSet.getString(2)));
                tuple.getValues().add(new TupleValue(requestData.nameFieldId(), resultSet.getString(3)));
                tuples.add(tuple);
            }
            return tuples;
        } catch (Exception ex) {
            throw new QueryExecutionException(
                    QUERY_FAILED + filterInstanceValuesQuery, ex);
        }
    }

    @Override
    public List<FilterNodeResponse> getFilterInstanceChildNodes(FilterChildNodesRequestData requestData) {
        final var queryBuilder = getQueryBuilder(requestData.dataSource().type());
        final var childNodesQuery = queryBuilder.getChildNodesQuery(requestData);
        final var result = new LinkedList<FilterNodeResponse>();

        log.debug(QUERY + childNodesQuery);

        try (
                var connection = poolManager.getConnection(requestData.dataSource());
                var statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                var resultSet = statement.executeQuery(childNodesQuery)
        ) {
            while (resultSet.next()) {
                result.add(new FilterNodeResponse(
                        requestData.responseFieldId(),
                        requestData.level() + 1,
                        resultSet.getString(1),
                        resultSet.getString(2),
                        null));
            }

            return result;
        } catch (Exception ex) {
            throw new QueryExecutionException(QUERY_FAILED + childNodesQuery, ex);
        }
    }

    @Override
    public List<Tuple> getFieldsValues(FilterRequestData requestData) {
        final var queryBuilder = getQueryBuilder(requestData.filter().dataSource().type());
        final var fieldsValuesQuery = queryBuilder.getFilterFieldsQuery(requestData);

        log.debug(QUERY + fieldsValuesQuery);
        List<Tuple> tuples = new LinkedList<>();

        try (
                var connection = poolManager.getConnection(requestData.filter().dataSource());
                var statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                var resultSet = statement.executeQuery(fieldsValuesQuery)
        ) {
            while (resultSet.next()) {
                var tuple = new Tuple();
                var count = new AtomicInteger(1);
                requestData.filter().fields().forEach(field -> {
                    try {
                        tuple.getValues().add(new TupleValue(field.fieldId(), resultSet.getString(count.getAndIncrement())));
                    } catch (SQLException ex) {
                        throw new QueryExecutionException("Error trying to get value from resultSet", ex);
                    }
                });
                tuples.add(tuple);
            }
            return tuples;
        } catch (Exception ex) {
            throw new QueryExecutionException(QUERY_FAILED + fieldsValuesQuery, ex);
        }
    }

    @Override
    public List<Map<String, String>> getQueryResult(DataSourceData dataSource, String query) {
        List<Map<String, String>> result = new LinkedList<>();
        try (
                var connection = poolManager.getConnection(dataSource);
                var statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                var resultSet = statement.executeQuery(query)
        ) {
            List<String> columnNames = getColumnNames(resultSet.getMetaData());
            while (resultSet.next()) {
                Map<String, String> row = new HashMap<>();
                for (var i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    row.put(columnNames.get(i - 1), resultSet.getString(i));
                }
                result.add(row);
            }
        } catch (SQLException ex) {
            throw new QueryExecutionException("Error trying to execute query:\n" + query);
        }

        return result;
    }

    @Override
    public void executeSql(DataSourceData dataSource, String query) {
        if (query == null || query.isEmpty() || query.trim().isEmpty()) return;

        log.debug("Trying to execute pre or post sql query:\n" + query);

        try (
                var connection = poolManager.getConnection(dataSource);
                var statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
        ) {
            statement.execute(query);
        } catch (SQLException ex) {
            log.error("SqlExecQueryImpl.executeSql(): Error trying to execute pre- or post- sql.", ex);
            throw new QueryExecutionException("Error trying to execute query:\n" + query);
        }

        log.debug("Pre or post sql executed successfully.");
    }

    private static List<String> getColumnNames(ResultSetMetaData metaData) throws SQLException {
        return IntStream
                .rangeClosed(1, metaData.getColumnCount())
                .mapToObj(i -> getColumnName(metaData, i))
                .collect(Collectors.toList());
    }

    private static String getColumnName(ResultSetMetaData metaData, int columnNumber) {
        String result = null;
        try {
            result = metaData.getColumnLabel(columnNumber).toLowerCase();
        } catch (SQLException ex) {
            log.error("Error trying to get column name");
        }
        return result;
    }

    private FilterQueryBuilder getQueryBuilder(DataSourceTypeEnum dataSourceType) {

        return queryBuilders
                .stream()
                .filter(o -> o.getClass().equals(builderTypes.get(dataSourceType)))
                .findFirst()
                .orElseThrow(() -> new InvalidParametersException("QueryBuilder not found for database type '" + dataSourceType + "'"));
    }
}
