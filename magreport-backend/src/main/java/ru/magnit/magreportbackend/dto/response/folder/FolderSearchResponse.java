package ru.magnit.magreportbackend.dto.response.folder;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

public record FolderSearchResponse(

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<Record> objects,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<FolderSearchResultResponse> folders
) {}