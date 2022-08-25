package ru.magnit.magreportbackend.dto.response.permission;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetFolderResponse;

import java.util.List;

public record DataSetFolderPermissionsResponse(

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        DataSetFolderResponse folder,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<RolePermissionResponse> rolePermissions
) {}
