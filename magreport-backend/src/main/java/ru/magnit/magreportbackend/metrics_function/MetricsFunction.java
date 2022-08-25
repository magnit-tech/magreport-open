package ru.magnit.magreportbackend.metrics_function;

public interface MetricsFunction {
    void addValue(String value);
    String calculate();
}
