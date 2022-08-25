package ru.magnit.magreportbackend.metrics_function.impl;

import ru.magnit.magreportbackend.metrics_function.MetricsFunction;

public class MaxIntegerFunction implements MetricsFunction {

    private Integer maxValue;

    @Override
    public void addValue(String value) {
        if (value != null)
            maxValue = maxValue == null ? Integer.parseInt(value) : Integer.max(maxValue, Integer.parseInt(value));
    }

    @Override
    public String calculate() {
        return String.valueOf(maxValue);
    }
}
