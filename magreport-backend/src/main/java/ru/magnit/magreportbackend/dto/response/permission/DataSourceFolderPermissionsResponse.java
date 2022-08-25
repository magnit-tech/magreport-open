package ru.magnit.magreportbackend.dto.response.permission;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceFolderResponse;

import java.util.List;

public record DataSourceFolderPermissionsResponse(

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        DataSourceFolderResponse folder,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<RolePermissionResponse> rolePermissions
) {
}
