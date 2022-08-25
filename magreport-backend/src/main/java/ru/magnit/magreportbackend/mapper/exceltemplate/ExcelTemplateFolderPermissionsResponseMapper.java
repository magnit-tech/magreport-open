package ru.magnit.magreportbackend.mapper.exceltemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplateFolder;
import ru.magnit.magreportbackend.dto.response.permission.ExcelTemplateFolderPermissionsResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ExcelTemplateFolderPermissionsResponseMapper implements Mapper<ExcelTemplateFolderPermissionsResponse, ExcelTemplateFolder> {

    private final ExcelTemplateFolderResponseMapper folderResponseMapper;
    private final RolePermissionResponseMapperExcelTemplate responseMapper;

    @Override
    public ExcelTemplateFolderPermissionsResponse from(ExcelTemplateFolder source) {
        return new ExcelTemplateFolderPermissionsResponse(
                folderResponseMapper.shallowMap(source),
                responseMapper.from(source.getFolderRoles()));
    }
}
