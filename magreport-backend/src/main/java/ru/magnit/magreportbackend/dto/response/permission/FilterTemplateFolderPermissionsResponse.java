package ru.magnit.magreportbackend.dto.response.permission;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTemplateFolderResponse;

import java.util.List;

public record FilterTemplateFolderPermissionsResponse(

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        FilterTemplateFolderResponse folder,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<RolePermissionResponse> rolePermissions
) {
}
