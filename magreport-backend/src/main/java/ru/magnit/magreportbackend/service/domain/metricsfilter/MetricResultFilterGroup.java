package ru.magnit.magreportbackend.service.domain.metricsfilter;

import ru.magnit.magreportbackend.domain.filterreport.GroupOperationTypeEnum;
import ru.magnit.magreportbackend.dto.inner.olap.MetricResult;
import ru.magnit.magreportbackend.dto.request.olap.MetricFilterGroup;

import java.util.Collections;
import java.util.List;

public class MetricResultFilterGroup implements MetricFilterNode {

    private List<MetricFilterNode> childNodes = Collections.emptyList();
    private GroupOperationTypeEnum operationType;
    private boolean invertResult;

    @Override
    public MetricFilterNode init(MetricResult[][][] metricResults, MetricFilterGroup filterGroup) {
        if (filterGroup.getChildGroups() != null && !filterGroup.getChildGroups().isEmpty()) {
            childNodes = filterGroup.getChildGroups().stream().map(childGroup -> MetricFilters.createFilter(metricResults, childGroup)).toList();
        } else {
            childNodes = filterGroup.getFilters().stream().map(filter -> MetricFilters.createFilter(metricResults, filter)).toList();
        }
        operationType = filterGroup.getOperationType();
        invertResult = filterGroup.isInvertResult();
        return this;
    }

    @Override
    public boolean filter(int column, int row) {
        boolean result;
        if (operationType == GroupOperationTypeEnum.AND) {
            result = true;
            for (MetricFilterNode childNode : childNodes) {
                result &= childNode.filter(column, row);
            }
        } else {
            result = false;
            for (MetricFilterNode childNode : childNodes) {
                result |= childNode.filter(column, row);
            }
        }
        return invertResult != result;
    }
}
