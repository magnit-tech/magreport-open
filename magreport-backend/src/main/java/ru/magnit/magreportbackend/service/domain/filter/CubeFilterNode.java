package ru.magnit.magreportbackend.service.domain.filter;

import ru.magnit.magreportbackend.dto.inner.olap.CubeData;
import ru.magnit.magreportbackend.dto.request.olap.FilterDefinition;
import ru.magnit.magreportbackend.dto.request.olap.FilterGroup;

public interface CubeFilterNode {
    default CubeFilterNode init (CubeData sourceCube, FilterGroup filterGroup) { return null; }
    default CubeFilterNode init (CubeData sourceCube, FilterDefinition filterDefinition){ return null; }
    boolean filter(int row);
}
