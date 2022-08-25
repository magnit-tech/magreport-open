package ru.magnit.magreportbackend.mapper.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.report.ReportFolderRolePermission;
import ru.magnit.magreportbackend.mapper.Cloner;

@Service
@RequiredArgsConstructor
public class ReportFolderRolePermissionCloner implements Cloner<ReportFolderRolePermission> {

    @Override
    public ReportFolderRolePermission clone(ReportFolderRolePermission source) {
        return new ReportFolderRolePermission()
                .setAuthority(source.getAuthority());
    }
}
