package ru.magnit.magreportbackend.dto.request.role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.dto.request.folder.FolderTypes;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RolePermissionAddRequest {

    private Long roleId;
    private Long folderId;
    private FolderTypes type;
    private List<FolderAuthorityEnum> permissions;

}
