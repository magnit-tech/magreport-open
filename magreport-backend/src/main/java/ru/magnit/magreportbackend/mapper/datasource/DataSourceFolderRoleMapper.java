package ru.magnit.magreportbackend.mapper.datasource;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolderRole;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.dto.request.folderreport.RoleAddPermissionRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class DataSourceFolderRoleMapper implements Mapper<DataSourceFolderRole, RoleAddPermissionRequest> {

    private final DataSourceFolderRolePermissionMapper permissionMapper;

    @Override
    public DataSourceFolderRole from(RoleAddPermissionRequest source) {
        var dataSourceFolderRole = new DataSourceFolderRole()
                .setRole(new Role(source.getRoleId()))
                .setPermissions(permissionMapper.from(source.getPermissions()));
        dataSourceFolderRole.getPermissions().forEach(perm->perm.setFolderRole(dataSourceFolderRole));

        return dataSourceFolderRole;
    }
}
