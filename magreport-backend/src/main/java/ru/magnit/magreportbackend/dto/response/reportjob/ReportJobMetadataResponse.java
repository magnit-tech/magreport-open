package ru.magnit.magreportbackend.dto.response.reportjob;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.dto.response.report.ReportFieldMetadataResponse;

import java.util.List;

public record ReportJobMetadataResponse(
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    Long id,

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    Long totalRows,

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    List<ReportFieldMetadataResponse> fields
    ) {}
