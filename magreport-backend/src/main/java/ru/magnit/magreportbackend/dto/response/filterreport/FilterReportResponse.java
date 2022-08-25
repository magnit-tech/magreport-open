package ru.magnit.magreportbackend.dto.response.filterreport;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;

import java.time.LocalDateTime;
import java.util.List;

import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

public record FilterReportResponse(

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Long id,

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Long filterInstanceId,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        FilterTypeEnum type,

        @JsonFormat(shape = JsonFormat.Shape.BOOLEAN)
        Boolean hidden,

        @JsonFormat(shape = JsonFormat.Shape.BOOLEAN)
        Boolean mandatory,

        @JsonFormat(shape = JsonFormat.Shape.BOOLEAN)
        Boolean rootSelectable,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String name,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String code,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String description,

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Integer ordinal,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<FilterReportFieldResponse> fields,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String userName,

        @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
        LocalDateTime created,

        @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
        LocalDateTime modified
) {

    public FilterReportResponse() {
        this(null, null, null, null, null,null, null, null, null, null, null, null, null, null);
    }
}
