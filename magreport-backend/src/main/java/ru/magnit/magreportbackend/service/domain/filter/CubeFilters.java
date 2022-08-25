package ru.magnit.magreportbackend.service.domain.filter;

import ru.magnit.magreportbackend.dto.inner.olap.CubeData;
import ru.magnit.magreportbackend.dto.request.olap.FilterDefinition;
import ru.magnit.magreportbackend.dto.request.olap.FilterGroup;

public class CubeFilters {
    private CubeFilters() {
    }

    public static CubeFilterNode createFilter(CubeData sourceCube, FilterGroup filterGroup) {
        if (filterGroup == null) return new EmptyFilter();
        return new CubeFilterGroup().init(sourceCube, filterGroup);
    }

    public static CubeFilterNode createFilter(CubeData sourceCube, FilterDefinition filterDefinition) {
        if (filterDefinition == null) return new EmptyFilter();

        return
                switch (filterDefinition.getFilterType()) {
                    case EMPTY -> new EmptyFilter();
                    case IN_LIST -> new CubeInListFilter().init(sourceCube, filterDefinition);
                    case CONTAINS_CS -> new CubeContainsCSFilter().init(sourceCube, filterDefinition);
                    case CONTAINS_CI -> new CubeContainsCIFilter().init(sourceCube, filterDefinition);
                    case EQUALS -> new CubeEqualsFilter().init(sourceCube, filterDefinition);
                    case GREATER -> new CubeGreaterFilter().init(sourceCube, filterDefinition);
                    case LESSER -> new CubeLesserFilter().init(sourceCube,filterDefinition);
                    case GREATER_OR_EQUALS -> new CubeGreaterOrEqualsFilter().init(sourceCube, filterDefinition);
                    case LESSER_OR_EQUALS -> new CubeLesserOrEqualsFilter().init(sourceCube, filterDefinition);
                    case BETWEEN -> new CubeBetweenFilter().init(sourceCube, filterDefinition);
                };
    }
}
