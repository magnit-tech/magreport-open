package ru.magnit.magreportbackend.metrics_function.impl;

import ru.magnit.magreportbackend.metrics_function.MetricsFunction;
import ru.magnit.magreportbackend.util.StringUtils;

public class MinStringFunction implements MetricsFunction {

    private String minValue;


    @Override
    public void addValue(String value) {
        minValue = minValue == null ? value : StringUtils.min(minValue,value);
    }

    @Override
    public String calculate() {
        return minValue;
    }
}
