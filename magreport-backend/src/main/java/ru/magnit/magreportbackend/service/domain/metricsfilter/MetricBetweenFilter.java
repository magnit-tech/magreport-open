package ru.magnit.magreportbackend.service.domain.metricsfilter;

import ru.magnit.magreportbackend.dto.inner.olap.MetricResult;
import ru.magnit.magreportbackend.dto.request.olap.MetricFilterDefinition;
import ru.magnit.magreportbackend.exception.InvalidParametersException;

public class MetricBetweenFilter implements MetricFilterNode {

    private boolean invert;
    private int startValue;
    private int stopValue;
    private MetricResult[][][] data;
    private int metric;

    @Override
    public MetricFilterNode init(MetricResult[][][] metricResults, MetricFilterDefinition filterDefinition) {
        invert = filterDefinition.isInvertResult();
        if (filterDefinition.getValues().isEmpty()) {
            startValue = Integer.MIN_VALUE;
            stopValue = Integer.MAX_VALUE;
        } else if (filterDefinition.getValues().size() == 2) {
            startValue = Integer.parseInt(filterDefinition.getValues().get(0));
            stopValue = Integer.parseInt(filterDefinition.getValues().get(1));
        } else {
            throw new InvalidParametersException("Incorrect number of filter arguments");
        }

        data = metricResults;
        metric = filterDefinition.getMetricId().intValue();
        return this;
    }

    @Override
    public boolean filter(int column, int row) {
        var value = Integer.parseInt(data[column][row][metric].getValue());
        return invert != ( startValue <= value && value <= stopValue);
    }
}
