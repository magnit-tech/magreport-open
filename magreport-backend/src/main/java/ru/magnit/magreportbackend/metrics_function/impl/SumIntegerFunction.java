package ru.magnit.magreportbackend.metrics_function.impl;

import ru.magnit.magreportbackend.metrics_function.MetricsFunction;

public class SumIntegerFunction implements MetricsFunction {

    private int sumOfValues;

    @Override
    public void addValue(String value) {
        if (value != null)
            sumOfValues += Integer.parseInt(value);
    }

    @Override
    public String calculate() {
        return String.valueOf(sumOfValues);
    }
}
