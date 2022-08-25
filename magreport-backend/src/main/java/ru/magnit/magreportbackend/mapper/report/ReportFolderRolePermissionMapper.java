package ru.magnit.magreportbackend.mapper.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthority;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.report.ReportFolderRolePermission;
import ru.magnit.magreportbackend.mapper.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportFolderRolePermissionMapper implements Mapper<ReportFolderRolePermission, FolderAuthorityEnum> {

    @Override
    public ReportFolderRolePermission from(FolderAuthorityEnum source) {
        return null;
    }

    @Override
    public List<ReportFolderRolePermission> from(List<FolderAuthorityEnum> sources) {
        return mapBaseProperties(sources);
    }

    private List<ReportFolderRolePermission> mapBaseProperties(List<FolderAuthorityEnum> sources) {

        return sources
                .stream()
                .map(perm -> new ReportFolderRolePermission()
                        .setAuthority(new FolderAuthority(perm)))
                .collect(Collectors.toList());
    }
}
