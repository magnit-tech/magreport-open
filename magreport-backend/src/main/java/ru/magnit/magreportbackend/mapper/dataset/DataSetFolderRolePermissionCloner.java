package ru.magnit.magreportbackend.mapper.dataset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolderRolePermission;
import ru.magnit.magreportbackend.mapper.Cloner;

@Service
@RequiredArgsConstructor
public class DataSetFolderRolePermissionCloner implements Cloner<DataSetFolderRolePermission> {
    @Override
    public DataSetFolderRolePermission clone(DataSetFolderRolePermission source) {
        return new DataSetFolderRolePermission()
                .setAuthority(source.getAuthority());
    }
}
