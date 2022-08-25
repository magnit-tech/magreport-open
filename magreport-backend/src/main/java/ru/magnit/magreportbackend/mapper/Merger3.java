package ru.magnit.magreportbackend.mapper;

public interface Merger3<T, S1, S2, S3> {

    T merge(S1 source1, S2 source2, S3 source3);
}
