package ru.magnit.magreportbackend.dto.response.permission;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceFolderResponse;

import java.util.List;

public record FilterInstanceFolderPermissionsResponse(

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        FilterInstanceFolderResponse folder,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<RolePermissionResponse> rolePermissions
) {
}
