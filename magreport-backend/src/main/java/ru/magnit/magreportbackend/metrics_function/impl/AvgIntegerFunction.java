package ru.magnit.magreportbackend.metrics_function.impl;

import ru.magnit.magreportbackend.metrics_function.MetricsFunction;

public class AvgIntegerFunction implements MetricsFunction {

    private int numValues;
    private int sumOfValues;

    @Override
    public void addValue(String value) {
        if (value != null) {
            numValues++;
            sumOfValues += Integer.parseInt(value);
        }
    }

    @Override
    public String calculate() {
        return (sumOfValues == 0) ? "0" : String.valueOf(sumOfValues / numValues);
    }
}
