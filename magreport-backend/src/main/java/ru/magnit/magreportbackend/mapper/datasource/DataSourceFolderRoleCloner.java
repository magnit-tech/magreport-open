package ru.magnit.magreportbackend.mapper.datasource;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolderRole;
import ru.magnit.magreportbackend.mapper.Cloner;

@Service
@RequiredArgsConstructor
public class DataSourceFolderRoleCloner implements Cloner<DataSourceFolderRole> {

    private final DataSourceFolderRolePermissionCloner dataSourceFolderRolePermissionCloner;

    @Override
    public DataSourceFolderRole clone(DataSourceFolderRole source) {
        var folderRole =  new DataSourceFolderRole()
                .setRole(source.getRole());

        var permissions = dataSourceFolderRolePermissionCloner.clone(source.getPermissions());
        permissions.forEach(p -> p.setFolderRole(folderRole));
        folderRole.setPermissions(permissions);

        return folderRole;
    }
}
