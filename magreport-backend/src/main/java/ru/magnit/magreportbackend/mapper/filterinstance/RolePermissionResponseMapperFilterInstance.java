package ru.magnit.magreportbackend.mapper.filterinstance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.BaseEntity;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceFolderRole;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceFolderRolePermission;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.dto.response.permission.RolePermissionResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.auth.RoleResponseMapper;

@Service
@RequiredArgsConstructor
public class RolePermissionResponseMapperFilterInstance implements Mapper<RolePermissionResponse, FilterInstanceFolderRole> {

    private final RoleResponseMapper roleResponseMapper;

    @Override
    public RolePermissionResponse from(FilterInstanceFolderRole source) {
        return new RolePermissionResponse(
            roleResponseMapper.from(source.getRole()),
            source.getPermissions()
                .stream()
                .map(FilterInstanceFolderRolePermission::getAuthority)
                .map(BaseEntity::getId)
                .map(FolderAuthorityEnum::getById).toList());
    }
}
