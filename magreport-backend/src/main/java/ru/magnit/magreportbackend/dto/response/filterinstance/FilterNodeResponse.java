package ru.magnit.magreportbackend.dto.response.filterinstance;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

public record FilterNodeResponse(

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Long fieldId,

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Long level,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String nodeId,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String nodeName,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<FilterNodeResponse> childNodes
) {}
