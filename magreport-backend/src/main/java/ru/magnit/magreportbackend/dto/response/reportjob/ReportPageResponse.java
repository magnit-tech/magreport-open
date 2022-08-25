package ru.magnit.magreportbackend.dto.response.reportjob;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;
import java.util.Map;

public record ReportPageResponse(

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        long reportId,

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        long jobId,

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        long pageNumber,

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        long rowCount,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<Map<String, String>> records
        ) {
}
