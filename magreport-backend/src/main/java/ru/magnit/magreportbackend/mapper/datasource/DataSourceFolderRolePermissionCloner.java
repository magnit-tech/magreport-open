package ru.magnit.magreportbackend.mapper.datasource;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolderRolePermission;
import ru.magnit.magreportbackend.mapper.Cloner;

@Service
@RequiredArgsConstructor
public class DataSourceFolderRolePermissionCloner implements Cloner<DataSourceFolderRolePermission> {
    @Override
    public DataSourceFolderRolePermission clone(DataSourceFolderRolePermission source) {
        return new DataSourceFolderRolePermission()
                .setAuthority(source.getAuthority());
    }
}
