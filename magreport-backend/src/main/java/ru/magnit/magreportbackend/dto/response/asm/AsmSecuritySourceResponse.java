package ru.magnit.magreportbackend.dto.response.asm;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceTypeEnum;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetResponse;

import java.time.LocalDateTime;
import java.util.List;

import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

public record AsmSecuritySourceResponse(

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Long id,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String name,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String description,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        ExternalAuthSourceTypeEnum sourceType,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        DataSetResponse dataSet,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String preSql,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String postSql,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<AsmSecuritySourceFieldResponse>fields,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<AsmSecurityFilterResponse> securityFilters,

        @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
        LocalDateTime created,

        @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
        LocalDateTime modified
) {}