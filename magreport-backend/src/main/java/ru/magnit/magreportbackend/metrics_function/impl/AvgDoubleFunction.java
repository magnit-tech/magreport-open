package ru.magnit.magreportbackend.metrics_function.impl;

import ru.magnit.magreportbackend.metrics_function.MetricsFunction;

public class AvgDoubleFunction implements MetricsFunction {

    private int numValues;
    private double sumOfValues;

    @Override
    public void addValue(String value) {

        if (value != null) {
            numValues++;
            sumOfValues = sumOfValues + Double.parseDouble(value);
        }
    }

    @Override
    public String calculate() {
        return String.valueOf(sumOfValues / numValues);
    }
}
