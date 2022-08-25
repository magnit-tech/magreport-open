package ru.magnit.magreportbackend.dto.response.permission;

import com.fasterxml.jackson.annotation.JsonFormat;

public record FolderPermissionCheckResponse(
        @JsonFormat(shape = JsonFormat.Shape.BOOLEAN)
        boolean canRead
) {}
