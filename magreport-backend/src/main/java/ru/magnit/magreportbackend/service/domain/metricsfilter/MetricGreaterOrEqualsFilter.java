package ru.magnit.magreportbackend.service.domain.metricsfilter;

import ru.magnit.magreportbackend.dto.inner.olap.MetricResult;
import ru.magnit.magreportbackend.dto.request.olap.MetricFilterDefinition;

public class MetricGreaterOrEqualsFilter implements MetricFilterNode {

    private boolean invert;
    private int value;
    private MetricResult[][][] data;
    private int metric;

    @Override
    public MetricFilterNode init(MetricResult[][][] metricResults, MetricFilterDefinition filterDefinition) {
        invert = filterDefinition.isInvertResult();
        value = filterDefinition.getValues().isEmpty() ? Integer.MIN_VALUE : Integer.parseInt(filterDefinition.getValues().get(0));
        data = metricResults;
        metric = filterDefinition.getMetricId().intValue();
        return this;
    }

    @Override
    public boolean filter(int column, int row) {
        return invert != (Integer.parseInt(data[column][row][metric].getValue()) >= value);
    }
}
