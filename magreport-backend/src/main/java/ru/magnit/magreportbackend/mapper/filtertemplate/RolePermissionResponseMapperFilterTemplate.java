package ru.magnit.magreportbackend.mapper.filtertemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.BaseEntity;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateFolderRole;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateFolderRolePermission;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.dto.response.permission.RolePermissionResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.auth.RoleResponseMapper;

@Service
@RequiredArgsConstructor
public class RolePermissionResponseMapperFilterTemplate implements Mapper<RolePermissionResponse, FilterTemplateFolderRole> {

    private final RoleResponseMapper roleResponseMapper;

    @Override
    public RolePermissionResponse from(FilterTemplateFolderRole source) {
        return new RolePermissionResponse(
            roleResponseMapper.from(source.getRole()),
            source.getPermissions()
                .stream()
                .map(FilterTemplateFolderRolePermission::getAuthority)
                .map(BaseEntity::getId)
                .map(FolderAuthorityEnum::getById).toList());
    }
}
