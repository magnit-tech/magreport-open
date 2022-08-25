package ru.magnit.magreportbackend.mapper;

public interface Merger2<T, S1, S2> {

    T merge(S1 source1, S2 source2);
}
