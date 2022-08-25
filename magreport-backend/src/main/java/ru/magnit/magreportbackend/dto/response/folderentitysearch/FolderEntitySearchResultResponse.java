package ru.magnit.magreportbackend.dto.response.folderentitysearch;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;

import java.util.List;

public record FolderEntitySearchResultResponse(

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<FolderNodeResponse> path,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        Object element
) {
}

