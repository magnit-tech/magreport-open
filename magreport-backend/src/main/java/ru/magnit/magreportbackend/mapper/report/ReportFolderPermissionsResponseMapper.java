package ru.magnit.magreportbackend.mapper.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.report.ReportFolder;
import ru.magnit.magreportbackend.dto.response.permission.ReportFolderPermissionsResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ReportFolderPermissionsResponseMapper implements Mapper<ReportFolderPermissionsResponse, ReportFolder> {

    private final ReportFolderResponseMapper folderResponseMapper;
    private final RolePermissionResponseMapperReportFolder responseMapper;

    @Override
    public ReportFolderPermissionsResponse from(ReportFolder source) {
        return new ReportFolderPermissionsResponse(
            folderResponseMapper.shallowMap(source),
            responseMapper.from(source.getFolderRoles()));
    }
}
