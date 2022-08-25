package ru.magnit.magreportbackend.dto.request.role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.magnit.magreportbackend.dto.request.folder.FolderTypes;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RolePermissionDeleteRequest {

    private Long folderId;
    private Long roleId;
    private FolderTypes type;
}
