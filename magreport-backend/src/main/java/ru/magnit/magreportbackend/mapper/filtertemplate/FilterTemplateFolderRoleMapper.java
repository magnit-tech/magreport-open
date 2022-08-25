package ru.magnit.magreportbackend.mapper.filtertemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateFolderRole;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.dto.request.folderreport.RoleAddPermissionRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FilterTemplateFolderRoleMapper implements Mapper<FilterTemplateFolderRole, RoleAddPermissionRequest> {

    private final FilterTemplateFolderRolePermissionMapper permissionMapper;

    @Override
    public FilterTemplateFolderRole from(RoleAddPermissionRequest source) {
        var filterTemplateFolderRole = new FilterTemplateFolderRole()
                .setRole(new Role(source.getRoleId()))
                .setPermissions(permissionMapper.from(source.getPermissions()));
        filterTemplateFolderRole.getPermissions().forEach(perm->perm.setFolderRole(filterTemplateFolderRole));

        return filterTemplateFolderRole;
    }
}
