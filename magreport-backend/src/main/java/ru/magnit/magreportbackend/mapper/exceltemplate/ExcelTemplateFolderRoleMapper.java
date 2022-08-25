package ru.magnit.magreportbackend.mapper.exceltemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplateFolderRole;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.dto.request.folderreport.RoleAddPermissionRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ExcelTemplateFolderRoleMapper implements Mapper<ExcelTemplateFolderRole, RoleAddPermissionRequest> {

    private final ExcelTemplateFolderRolePermissionMapper permissionMapper;

    @Override
    public ExcelTemplateFolderRole from(RoleAddPermissionRequest source) {
        var excelTemplateFolderRole = new ExcelTemplateFolderRole()
                .setRole(new Role(source.getRoleId()))
                .setPermissions(permissionMapper.from(source.getPermissions()));
        excelTemplateFolderRole.getPermissions().forEach(perm->perm.setFolderRole(excelTemplateFolderRole));

        return excelTemplateFolderRole;
    }
}
