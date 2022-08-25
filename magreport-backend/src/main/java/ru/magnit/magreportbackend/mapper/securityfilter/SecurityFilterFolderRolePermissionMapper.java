package ru.magnit.magreportbackend.mapper.securityfilter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthority;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterFolderRolePermission;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class SecurityFilterFolderRolePermissionMapper implements Mapper<SecurityFilterFolderRolePermission, FolderAuthorityEnum> {

    @Override
    public SecurityFilterFolderRolePermission from(FolderAuthorityEnum source) {

        return new SecurityFilterFolderRolePermission()
                .setAuthority(new FolderAuthority(source));
    }
}
