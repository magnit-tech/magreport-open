package ru.magnit.magreportbackend.dto.response.permission;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.dto.response.folderreport.FolderResponse;

import java.util.List;

public record FolderPermissionsResponse(

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        FolderResponse folder,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<RolePermissionResponse> rolePermissions
) {}