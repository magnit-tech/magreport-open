package ru.magnit.magreportbackend.mapper.securityfilter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterFolderRole;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.dto.request.folderreport.RoleAddPermissionRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class SecurityFilterFolderRoleMapper implements Mapper<SecurityFilterFolderRole, RoleAddPermissionRequest> {

    private final SecurityFilterFolderRolePermissionMapper permissionMapper;

    @Override
    public SecurityFilterFolderRole from(RoleAddPermissionRequest source) {
        var folderRole = new SecurityFilterFolderRole()
            .setRole(new Role(source.getRoleId()))
            .setPermissions(permissionMapper.from(source.getPermissions()));
        folderRole.getPermissions().forEach(perm -> perm.setFolderRole(folderRole));

        return folderRole;
    }
}
