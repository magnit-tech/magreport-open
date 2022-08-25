package ru.magnit.magreportbackend.dto.response.filterinstance;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.dto.tuple.Tuple;

import java.util.List;

public record FilterInstanceValuesResponse(

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        FilterInstanceResponse filter,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<Tuple> tuples
) {}
