package ru.magnit.magreportbackend.dto.response.filterreport;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

public record FilterReportValuesCheckResponse(
        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        FilterReportResponse filter,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<String> values
        ) {
}
