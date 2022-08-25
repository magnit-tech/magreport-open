package ru.magnit.magreportbackend.dto.response.report;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ReportFieldMetadataResponse(
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    Long id,

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    String type,

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    String name,

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    String description,

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    Integer ordinal,

    @JsonFormat(shape = JsonFormat.Shape.BOOLEAN)
    Boolean visible
) {}
