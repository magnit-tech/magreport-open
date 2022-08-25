package ru.magnit.magreportbackend.mapper.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.report.ReportFolderRole;
import ru.magnit.magreportbackend.mapper.Cloner;

@Service
@RequiredArgsConstructor
public class ReportFolderRoleCloner implements Cloner<ReportFolderRole> {

    private final ReportFolderRolePermissionCloner reportFolderRolePermissionClone;

    @Override
    public ReportFolderRole clone(ReportFolderRole source) {
        var folderRole = new ReportFolderRole()
                .setRole(source.getRole());

        var permission = reportFolderRolePermissionClone.clone(source.getPermissions());
        permission.forEach(p -> p.setFolderRole(folderRole));
        folderRole.setPermissions(permission);

        return folderRole;
    }
}
