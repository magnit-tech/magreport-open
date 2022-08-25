package ru.magnit.magreportbackend.dto.response.securityfilter;

import com.fasterxml.jackson.annotation.JsonFormat;

public record FieldMappingResponse(

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Long filterInstanceFieldId,

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Long dataSetFieldId
) {
}
