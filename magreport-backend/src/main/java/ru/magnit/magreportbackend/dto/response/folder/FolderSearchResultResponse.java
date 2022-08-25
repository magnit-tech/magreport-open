package ru.magnit.magreportbackend.dto.response.folder;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

public record FolderSearchResultResponse(

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<FolderNodeResponse> path,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        FolderNodeResponse folder
) {}