package ru.magnit.magreportbackend.mapper.exceltemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplateFolderRolePermission;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthority;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ExcelTemplateFolderRolePermissionMapper implements Mapper<ExcelTemplateFolderRolePermission, FolderAuthorityEnum> {

    @Override
    public ExcelTemplateFolderRolePermission from(FolderAuthorityEnum source) {
        return new ExcelTemplateFolderRolePermission()
                .setAuthority(new FolderAuthority(source));
    }
}
