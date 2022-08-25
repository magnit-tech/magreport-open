package ru.magnit.magreportbackend.dto.response.filterreport;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.domain.filterreport.GroupOperationTypeEnum;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

public record FilterGroupResponse(

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Long id,

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Long reportId,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String code,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String name,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String description,

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Long ordinal,

        @JsonFormat(shape = JsonFormat.Shape.BOOLEAN)
        Boolean linkedFilters,

        @JsonFormat(shape = JsonFormat.Shape.BOOLEAN)
        Boolean mandatory,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        GroupOperationTypeEnum operationType,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<FilterGroupResponse> childGroups,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<FilterReportResponse> filters,

        @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
        LocalDateTime created,

        @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
        LocalDateTime modified
) {

    public FilterGroupResponse() {
        this(null,null,null, null, null, null, null, null, null, Collections.emptyList(), Collections.emptyList(), null, null);
    }
}
