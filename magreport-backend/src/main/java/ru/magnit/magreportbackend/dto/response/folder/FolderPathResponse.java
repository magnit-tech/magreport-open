package ru.magnit.magreportbackend.dto.response.folder;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

public record FolderPathResponse(
        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<FolderNodeResponse> path
) {}
