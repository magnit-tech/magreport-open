package ru.magnit.magreportbackend.dto.response.asm;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceFieldTypeEnum;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetFieldResponse;

import java.time.LocalDateTime;
import java.util.List;

import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

public record AsmSecuritySourceFieldResponse(

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Long id,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        ExternalAuthSourceFieldTypeEnum fieldType,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        DataSetFieldResponse dataSetField,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<AsmFilterInstanceFieldResponse> filterInstanceFields,

        @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
        LocalDateTime created,

        @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
        LocalDateTime modified
        ) {
}