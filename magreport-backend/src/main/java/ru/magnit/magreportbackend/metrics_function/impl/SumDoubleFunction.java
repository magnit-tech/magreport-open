package ru.magnit.magreportbackend.metrics_function.impl;

import ru.magnit.magreportbackend.metrics_function.MetricsFunction;

public class SumDoubleFunction implements MetricsFunction {

    private double sumOfValues;


    @Override
    public void addValue(String value) {
        if (value != null)
            sumOfValues += Double.parseDouble(value);
    }

    @Override
    public String calculate() {
        return String.valueOf(sumOfValues);
    }
}
