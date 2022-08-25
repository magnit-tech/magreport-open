package ru.magnit.magreportbackend.mapper.dataset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolderRolePermission;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthority;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class DataSetFolderRolePermissionMapper implements Mapper<DataSetFolderRolePermission, FolderAuthorityEnum> {

    @Override
    public DataSetFolderRolePermission from(FolderAuthorityEnum source) {
        return new DataSetFolderRolePermission()
                .setAuthority(new FolderAuthority(source));
    }
}
