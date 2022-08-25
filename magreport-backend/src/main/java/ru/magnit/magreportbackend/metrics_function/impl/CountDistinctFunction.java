package ru.magnit.magreportbackend.metrics_function.impl;

import ru.magnit.magreportbackend.metrics_function.MetricsFunction;

import java.util.HashSet;
import java.util.Set;

public class CountDistinctFunction implements MetricsFunction {

    private final Set<String> values = new HashSet<>();

    @Override
    public void addValue(String value) {
        values.add(value);
    }

    @Override
    public String calculate() {
        return Integer.toString(values.size());
    }
}
