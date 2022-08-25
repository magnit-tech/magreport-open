package ru.magnit.magreportbackend.mapper.folderreport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.folderreport.Folder;
import ru.magnit.magreportbackend.domain.folderreport.FolderRole;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.dto.inner.folderreport.FolderRoleView;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FolderRoleMapper implements Mapper<FolderRole, FolderRoleView> {

    private final FolderRolePermissionMapper folderRolePermissionMapper;

    @Override
    public FolderRole from(FolderRoleView source) {

        var folderRole = mapBaseProperties(source);

        folderRole.setPermissions(folderRolePermissionMapper.from(source.getPermissions()));
        folderRole.getPermissions().forEach(perm -> perm.setFolderRole(folderRole));

        return folderRole;
    }

    private FolderRole mapBaseProperties(FolderRoleView source) {

        return new FolderRole()
                .setFolder(new Folder(source.getFolderId()))
                .setRole(new Role(source.getRoleId()));
    }
}
