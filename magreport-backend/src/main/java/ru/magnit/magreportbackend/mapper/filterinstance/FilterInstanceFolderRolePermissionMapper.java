package ru.magnit.magreportbackend.mapper.filterinstance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceFolderRolePermission;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthority;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FilterInstanceFolderRolePermissionMapper implements Mapper<FilterInstanceFolderRolePermission, FolderAuthorityEnum> {

    @Override
    public FilterInstanceFolderRolePermission from(FolderAuthorityEnum source) {
        return new FilterInstanceFolderRolePermission()
                .setAuthority(new FolderAuthority(source));
    }
}
