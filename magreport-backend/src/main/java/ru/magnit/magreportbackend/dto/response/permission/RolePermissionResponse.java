package ru.magnit.magreportbackend.dto.response.permission;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;

import java.util.List;

public record RolePermissionResponse(

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        RoleResponse role,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        List<FolderAuthorityEnum> permissions
) {}
