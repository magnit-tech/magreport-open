package ru.magnit.magreportbackend.dto.response.report;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ReportShortResponse(

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Long id,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String name
) {
}
