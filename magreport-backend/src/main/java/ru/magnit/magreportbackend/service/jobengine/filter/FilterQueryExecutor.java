package ru.magnit.magreportbackend.service.jobengine.filter;

import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;
import ru.magnit.magreportbackend.dto.inner.filter.FilterChildNodesRequestData;
import ru.magnit.magreportbackend.dto.inner.filter.FilterRequestData;
import ru.magnit.magreportbackend.dto.inner.filter.FilterValueListRequestData;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterNodeResponse;
import ru.magnit.magreportbackend.dto.tuple.Tuple;

import java.util.List;
import java.util.Map;

public interface FilterQueryExecutor {

    List<Tuple> getFilterInstanceValuesQuery(FilterValueListRequestData requestData);

    List<FilterNodeResponse> getFilterInstanceChildNodes(FilterChildNodesRequestData requestData);

    List<Tuple> getFieldsValues(FilterRequestData filterRequestData);

    List<Map<String, String>> getQueryResult(DataSourceData dataSource, String query);

    void executeSql(DataSourceData dataSource, String query);
}
