package ru.magnit.magreportbackend.mapper.datasource;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.BaseEntity;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolderRole;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolderRolePermission;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.dto.response.permission.RolePermissionResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.auth.RoleResponseMapper;

@Service
@RequiredArgsConstructor
public class RolePermissionResponseMapperDataSource implements Mapper<RolePermissionResponse, DataSourceFolderRole> {

    private final RoleResponseMapper roleResponseMapper;

    @Override
    public RolePermissionResponse from(DataSourceFolderRole source) {
        return new RolePermissionResponse(
            roleResponseMapper.from(source.getRole()),
            source.getPermissions()
                .stream()
                .map(DataSourceFolderRolePermission::getAuthority)
                .map(BaseEntity::getId)
                .map(FolderAuthorityEnum::getById).toList());
    }
}
