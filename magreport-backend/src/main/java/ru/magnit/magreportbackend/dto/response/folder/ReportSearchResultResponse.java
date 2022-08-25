package ru.magnit.magreportbackend.dto.response.folder;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.dto.response.report.ReportResponse;

import java.util.List;


public record ReportSearchResultResponse(

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<FolderNodeResponse> path,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        ReportResponse element
) {}