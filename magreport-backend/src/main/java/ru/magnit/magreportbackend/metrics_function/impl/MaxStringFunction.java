package ru.magnit.magreportbackend.metrics_function.impl;

import ru.magnit.magreportbackend.metrics_function.MetricsFunction;
import ru.magnit.magreportbackend.util.StringUtils;

public class MaxStringFunction implements MetricsFunction {

    private String maxValue;

    @Override
    public void addValue(String value){
        maxValue = maxValue == null ? value :  StringUtils.max(maxValue,value);
    }

    @Override
    public String calculate() {
        return maxValue;
    }
}
