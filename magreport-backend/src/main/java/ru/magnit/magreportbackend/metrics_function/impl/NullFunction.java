package ru.magnit.magreportbackend.metrics_function.impl;

import ru.magnit.magreportbackend.exception.NotImplementedException;
import ru.magnit.magreportbackend.metrics_function.MetricsFunction;

public class NullFunction implements MetricsFunction {

    @Override
    public void addValue(String value) {
        throw new NotImplementedException("This function must not be called.");
    }

    @Override
    public String calculate() {
        return "";
    }
}
