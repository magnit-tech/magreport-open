package ru.magnit.magreportbackend.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface Cloner <S> {
    S clone(S source);

    default List<S> clone (List<S> sources) {
        return sources.stream().map(this::clone).collect(Collectors.toCollection(ArrayList::new));
    }

    default S clone(S source, Object context) {
        return clone(source);
    }

    default List<S> clone (List<S> sources, Object context) {
        return sources.stream().map(source -> clone(source, context)).collect(Collectors.toCollection(ArrayList::new));
    }
}
