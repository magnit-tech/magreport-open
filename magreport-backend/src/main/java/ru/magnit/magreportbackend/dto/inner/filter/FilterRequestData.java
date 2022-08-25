package ru.magnit.magreportbackend.dto.inner.filter;

import ru.magnit.magreportbackend.dto.tuple.Tuple;
import ru.magnit.magreportbackend.exception.InvalidParametersException;

import java.util.List;

public record FilterRequestData(
        FilterData filter,
        List<Tuple> tuples
) {

    public Long getFieldId() {
        return tuples
                .stream()
                .flatMap(tuple -> tuple.getValues().stream())
                .findFirst()
                .orElseThrow(() -> new InvalidParametersException("Empty filter parameters:\n" + this))
                .getFieldId();
    }
}
