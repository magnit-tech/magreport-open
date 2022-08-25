package ru.magnit.magreportbackend.mapper;

public interface Merger<T, S> {

    T merge(T target, S source);
}
