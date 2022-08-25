package ru.magnit.magreportbackend.metrics_function.impl;

import ru.magnit.magreportbackend.metrics_function.MetricsFunction;

public class MinIntegerFunction implements MetricsFunction {

    private Integer minValue;

    @Override
    public void addValue(String value) {
        if (value != null)
            minValue = minValue== null ? Integer.parseInt(value) : Integer.min(minValue, Integer.parseInt(value));

    }

    @Override
    public String calculate() {
        return String.valueOf(minValue);
    }
}
