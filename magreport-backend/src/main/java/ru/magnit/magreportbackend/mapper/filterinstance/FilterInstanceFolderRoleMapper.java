package ru.magnit.magreportbackend.mapper.filterinstance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceFolderRole;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.dto.request.folderreport.RoleAddPermissionRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FilterInstanceFolderRoleMapper implements Mapper<FilterInstanceFolderRole, RoleAddPermissionRequest> {

    private final FilterInstanceFolderRolePermissionMapper permissionMapper;

    @Override
    public FilterInstanceFolderRole from(RoleAddPermissionRequest source) {
        var dataSetFolderRole = new FilterInstanceFolderRole()
                .setRole(new Role(source.getRoleId()))
                .setPermissions(permissionMapper.from(source.getPermissions()));
        dataSetFolderRole.getPermissions().forEach(perm->perm.setFolderRole(dataSetFolderRole));

        return dataSetFolderRole;
    }
}
