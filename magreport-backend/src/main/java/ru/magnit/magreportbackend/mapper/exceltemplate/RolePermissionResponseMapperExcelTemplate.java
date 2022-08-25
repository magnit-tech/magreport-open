package ru.magnit.magreportbackend.mapper.exceltemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.BaseEntity;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplateFolderRole;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplateFolderRolePermission;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.dto.response.permission.RolePermissionResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.auth.RoleResponseMapper;

@Service
@RequiredArgsConstructor
public class RolePermissionResponseMapperExcelTemplate implements Mapper<RolePermissionResponse, ExcelTemplateFolderRole> {

    private final RoleResponseMapper roleResponseMapper;

    @Override
    public RolePermissionResponse from(ExcelTemplateFolderRole source) {
        return new RolePermissionResponse(
            roleResponseMapper.from(source.getRole()),
            source.getPermissions()
                .stream()
                .map(ExcelTemplateFolderRolePermission::getAuthority)
                .map(BaseEntity::getId)
                .map(FolderAuthorityEnum::getById).toList());
    }
}
