package ru.magnit.magreportbackend.mapper.filtertemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateFolderRolePermission;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthority;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FilterTemplateFolderRolePermissionMapper implements Mapper<FilterTemplateFolderRolePermission, FolderAuthorityEnum> {

    @Override
    public FilterTemplateFolderRolePermission from(FolderAuthorityEnum source) {
        return new FilterTemplateFolderRolePermission()
                .setAuthority(new FolderAuthority(source));
    }
}
