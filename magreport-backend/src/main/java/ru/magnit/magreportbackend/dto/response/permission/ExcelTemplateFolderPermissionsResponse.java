package ru.magnit.magreportbackend.dto.response.permission;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ExcelTemplateFolderResponse;

import java.util.List;

public record ExcelTemplateFolderPermissionsResponse(

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        ExcelTemplateFolderResponse folder,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<RolePermissionResponse> rolePermissions
) {
}
