package ru.magnit.magreportbackend.dto.response.filterinstance;

import com.fasterxml.jackson.annotation.JsonFormat;

public record FilterInstanceChildNodesResponse(

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        FilterInstanceResponse filter,

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Long fieldId,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        FilterNodeResponse rootNode
) {}