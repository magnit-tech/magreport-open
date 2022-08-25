package ru.magnit.magreportbackend.mapper.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.report.ReportFolder;
import ru.magnit.magreportbackend.domain.report.ReportFolderRole;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.dto.inner.folderreport.FolderRoleView;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ReportFolderRoleMapper implements Mapper<ReportFolderRole, FolderRoleView> {

    private final ReportFolderRolePermissionMapper reportFolderRolePermissionMapper;

    @Override
    public ReportFolderRole from(FolderRoleView source) {

        var folderRole = mapBaseProperties(source);

        folderRole.setPermissions(reportFolderRolePermissionMapper.from(source.getPermissions()));
        folderRole.getPermissions().forEach(perm -> perm.setFolderRole(folderRole));

        return folderRole;
    }

    private ReportFolderRole mapBaseProperties(FolderRoleView source) {

        return new ReportFolderRole()
                .setFolder(new ReportFolder(source.getFolderId()))
                .setRole(new Role(source.getRoleId()));
    }
}
