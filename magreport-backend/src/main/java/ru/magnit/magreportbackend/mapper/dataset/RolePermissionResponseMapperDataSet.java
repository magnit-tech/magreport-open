package ru.magnit.magreportbackend.mapper.dataset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.BaseEntity;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolderRole;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolderRolePermission;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.dto.response.permission.RolePermissionResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.auth.RoleResponseMapper;

@Service
@RequiredArgsConstructor
public class RolePermissionResponseMapperDataSet implements Mapper<RolePermissionResponse, DataSetFolderRole> {

    private final RoleResponseMapper roleResponseMapper;

    @Override
    public RolePermissionResponse from(DataSetFolderRole source) {
        return new RolePermissionResponse(
                roleResponseMapper.from(source.getRole()),
                source.getPermissions()
                        .stream()
                        .map(DataSetFolderRolePermission::getAuthority)
                        .map(BaseEntity::getId)
                        .map(FolderAuthorityEnum::getById)
                        .toList());
    }
}
