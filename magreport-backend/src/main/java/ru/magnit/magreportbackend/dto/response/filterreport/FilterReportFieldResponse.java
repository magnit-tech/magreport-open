package ru.magnit.magreportbackend.dto.response.filterreport;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;

import java.time.LocalDateTime;

import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

public record FilterReportFieldResponse(

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Long id,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String name,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String description,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        FilterFieldTypeEnum type,

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Long level,

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Long ordinal,

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Long filterInstanceFieldId,

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Long reportFieldId,

        @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
        LocalDateTime created,

        @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
        LocalDateTime modified,

        @JsonFormat(shape = JsonFormat.Shape.BOOLEAN)
        Boolean expand
) {
}
