package ru.magnit.magreportbackend.service.domain.metricsfilter;

import ru.magnit.magreportbackend.dto.inner.olap.MetricResult;
import ru.magnit.magreportbackend.dto.request.olap.MetricFilterDefinition;
import ru.magnit.magreportbackend.dto.request.olap.MetricFilterGroup;

public interface MetricFilterNode {

    default MetricFilterNode init(MetricResult[][][] metricResults, MetricFilterGroup filterGroup) {return null;}
    default MetricFilterNode init(MetricResult[][][] metricResults, MetricFilterDefinition filterDefinition) {return null;}
    boolean filter(int column, int row);

}
