package ru.magnit.magreportbackend.mapper.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.BaseEntity;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.report.ReportFolderRole;
import ru.magnit.magreportbackend.domain.report.ReportFolderRolePermission;
import ru.magnit.magreportbackend.dto.response.permission.RolePermissionResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.auth.RoleResponseMapper;

@Service
@RequiredArgsConstructor
public class RolePermissionResponseMapperReportFolder implements Mapper<RolePermissionResponse, ReportFolderRole> {

    private final RoleResponseMapper roleResponseMapper;

    @Override
    public RolePermissionResponse from(ReportFolderRole source) {
        return new RolePermissionResponse(
            roleResponseMapper.from(source.getRole()),
            source.getPermissions()
                .stream()
                .map(ReportFolderRolePermission::getAuthority)
                .map(BaseEntity::getId)
                .map(FolderAuthorityEnum::getById).toList()
        );
    }
}
