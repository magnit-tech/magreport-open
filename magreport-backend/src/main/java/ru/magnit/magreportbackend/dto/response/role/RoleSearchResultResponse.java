package ru.magnit.magreportbackend.dto.response.role;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;

import java.util.List;

public record RoleSearchResultResponse(
        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<FolderNodeResponse> path,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        RoleResponse element
) {
}
