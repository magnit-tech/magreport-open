package ru.magnit.magreportbackend.mapper.datasource;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolderRolePermission;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthority;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class DataSourceFolderRolePermissionMapper implements Mapper<DataSourceFolderRolePermission, FolderAuthorityEnum> {

    @Override
    public DataSourceFolderRolePermission from(FolderAuthorityEnum source) {
        return new DataSourceFolderRolePermission()
                .setAuthority(new FolderAuthority(source));
    }
}
