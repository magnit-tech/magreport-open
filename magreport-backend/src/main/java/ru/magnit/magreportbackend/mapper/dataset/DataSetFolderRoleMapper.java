package ru.magnit.magreportbackend.mapper.dataset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolderRole;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.dto.request.folderreport.RoleAddPermissionRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class DataSetFolderRoleMapper implements Mapper<DataSetFolderRole, RoleAddPermissionRequest> {

    private final DataSetFolderRolePermissionMapper permissionMapper;

    @Override
    public DataSetFolderRole from(RoleAddPermissionRequest source) {
        var dataSetFolderRole = new DataSetFolderRole()
                .setRole(new Role(source.getRoleId()))
                .setPermissions(permissionMapper.from(source.getPermissions()));
        dataSetFolderRole.getPermissions().forEach(perm->perm.setFolderRole(dataSetFolderRole));

        return dataSetFolderRole;
    }
}
