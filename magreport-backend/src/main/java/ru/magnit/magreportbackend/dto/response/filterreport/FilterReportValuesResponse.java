package ru.magnit.magreportbackend.dto.response.filterreport;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.dto.tuple.Tuple;

import java.util.List;

public record FilterReportValuesResponse(
        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        FilterReportResponse filter,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<Tuple> tuples
) {}