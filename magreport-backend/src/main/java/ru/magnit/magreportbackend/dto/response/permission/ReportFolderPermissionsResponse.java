package ru.magnit.magreportbackend.dto.response.permission;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.dto.response.report.ReportFolderResponse;

import java.util.List;

public record ReportFolderPermissionsResponse(

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        ReportFolderResponse folder,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<RolePermissionResponse> rolePermissions
) {}
