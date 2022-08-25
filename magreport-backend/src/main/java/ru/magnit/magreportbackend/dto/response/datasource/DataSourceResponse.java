package ru.magnit.magreportbackend.dto.response.datasource;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;


public record DataSourceResponse(

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Long id,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String name,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String description,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String url,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String userName,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        DataSourceTypeResponse type,

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Short poolSize,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String creator,

        @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
        LocalDateTime created,

        @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
        LocalDateTime modified
) {
    // For satisfying tests (default constructor with meaningless parameters)
    public DataSourceResponse() {
        this(null, null, null, null, null, null, null, null, null, null);
    }
}
