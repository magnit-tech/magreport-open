package ru.magnit.magreportbackend.service.domain.metricsfilter;

public class EmptyFilter implements MetricFilterNode{
    @Override
    public boolean filter(int column, int row) {
        return true;
    }
}
