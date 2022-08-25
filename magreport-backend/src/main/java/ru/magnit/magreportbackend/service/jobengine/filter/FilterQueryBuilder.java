package ru.magnit.magreportbackend.service.jobengine.filter;

import ru.magnit.magreportbackend.dto.inner.filter.FilterChildNodesRequestData;
import ru.magnit.magreportbackend.dto.inner.filter.FilterRequestData;
import ru.magnit.magreportbackend.dto.inner.filter.FilterValueListRequestData;

public interface FilterQueryBuilder {

    String getFilterValuesQuery(FilterValueListRequestData requestData);

    String getChildNodesQuery(FilterChildNodesRequestData requestData);

    String getFilterFieldsQuery(FilterRequestData requestData);
}
