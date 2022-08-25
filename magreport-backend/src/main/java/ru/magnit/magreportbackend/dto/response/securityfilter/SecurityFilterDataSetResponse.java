package ru.magnit.magreportbackend.dto.response.securityfilter;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetResponse;

import java.util.List;

public record SecurityFilterDataSetResponse(

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        DataSetResponse dataSet,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<FieldMappingResponse> fields
) {
}
