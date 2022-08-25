package ru.magnit.magreportbackend.service.domain.filter;

import ru.magnit.magreportbackend.domain.filterreport.GroupOperationTypeEnum;
import ru.magnit.magreportbackend.dto.inner.olap.CubeData;
import ru.magnit.magreportbackend.dto.request.olap.FilterGroup;

import java.util.Collections;
import java.util.List;

public class CubeFilterGroup implements CubeFilterNode{

    private List<CubeFilterNode> childNodes = Collections.emptyList();
    private GroupOperationTypeEnum operationType;
    private boolean invertResult;

    @Override
    public CubeFilterNode init(CubeData sourceCube, FilterGroup filterGroup) {
        if (filterGroup.getChildGroups() != null && !filterGroup.getChildGroups().isEmpty()) {
            childNodes = filterGroup.getChildGroups().stream().map(childGroup -> CubeFilters.createFilter(sourceCube, childGroup)).toList();
        } else {
            childNodes = filterGroup.getFilters().stream().map(filter -> CubeFilters.createFilter(sourceCube, filter)).toList();
        }
        operationType = filterGroup.getOperationType();
        invertResult = filterGroup.isInvertResult();
        return this;
    }

    @Override
    public boolean filter(int row) {
        boolean result;
        if (operationType == GroupOperationTypeEnum.AND) {
            result = true;
            for (CubeFilterNode childNode : childNodes) {
                result &= childNode.filter(row);
            }
        } else {
            result = false;
            for (CubeFilterNode childNode : childNodes) {
                result |= childNode.filter(row);
            }
        }
        return invertResult != result;
    }
}
