package ru.magnit.magreportbackend.mapper;

import ru.magnit.magreportbackend.exception.NotImplementedException;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public interface Mapper<T, S> {

    T from(S source);

    default List<T> from (List<S> sources) {
        return sources.stream().map(this::from).collect(Collectors.toCollection(LinkedList::new));
    }

    default T shallowMap(S source) {
        throw new NotImplementedException("Method shallowCopy not implemented yet");
    }

    default List<T> shallowMap(List<S> sources) {
        return sources.stream().map(this::shallowMap).collect(Collectors.toCollection(LinkedList::new));
    }
}
