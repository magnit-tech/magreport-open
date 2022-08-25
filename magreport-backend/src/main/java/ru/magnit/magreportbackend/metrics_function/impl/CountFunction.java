package ru.magnit.magreportbackend.metrics_function.impl;

import ru.magnit.magreportbackend.metrics_function.MetricsFunction;

public class CountFunction implements MetricsFunction {

    private int numValues;

    @Override
    public void addValue(String value) {
        numValues++;
    }

    @Override
    public String calculate() {
        return Integer.toString(numValues).intern();
    }
}
