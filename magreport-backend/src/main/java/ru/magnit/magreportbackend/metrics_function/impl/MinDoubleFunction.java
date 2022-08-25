package ru.magnit.magreportbackend.metrics_function.impl;

import ru.magnit.magreportbackend.metrics_function.MetricsFunction;

public class MinDoubleFunction implements MetricsFunction {

    private Double minValue;

    @Override
    public void addValue(String value) {
        if (value != null)
            minValue = minValue == null ? Double.parseDouble(value) : Double.min(minValue, Double.parseDouble(value));
    }

    @Override
    public String calculate() {
        return String.valueOf(minValue);
    }
}
