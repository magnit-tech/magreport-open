package ru.magnit.magreportbackend.dto.response.filterreport;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterNodeResponse;

public record FilterReportChildNodesResponse(

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        FilterReportResponse filter,

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Long fieldId,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        FilterNodeResponse rootNode
) {}
