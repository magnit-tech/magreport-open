package ru.magnit.magreportbackend.mapper.dataset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolderRole;
import ru.magnit.magreportbackend.mapper.Cloner;

@Service
@RequiredArgsConstructor
public class DataSetFolderRoleCloner implements Cloner<DataSetFolderRole> {

    private final DataSetFolderRolePermissionCloner dataSetFolderRolePermissionCloner;

    @Override
    public DataSetFolderRole clone(DataSetFolderRole source) {
       var folderRole = new DataSetFolderRole()
                .setRole(source.getRole());

       var permissions = dataSetFolderRolePermissionCloner.clone(source.getPermissions());
       permissions.forEach(p-> p.setFolderRole(folderRole));

       return folderRole;
    }
}
