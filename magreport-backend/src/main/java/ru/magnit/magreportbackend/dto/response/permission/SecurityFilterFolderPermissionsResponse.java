package ru.magnit.magreportbackend.dto.response.permission;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterFolderResponse;

import java.util.List;

public record SecurityFilterFolderPermissionsResponse(

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        SecurityFilterFolderResponse folder,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<RolePermissionResponse> rolePermissions
) {
}
