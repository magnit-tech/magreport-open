package ru.magnit.magreportbackend.mapper.exceltemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplateFolder;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ExcelTemplateFolderResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ExcelTemplateFolderResponseMapper implements Mapper<ExcelTemplateFolderResponse, ExcelTemplateFolder> {

    private final ExcelTemplateResponseMapper excelTemplateResponseMapper;

    @Override
    public ExcelTemplateFolderResponse from(ExcelTemplateFolder source) {
        var folderResponse = mapBaseProperties(source);
        folderResponse.setExcelTemplates(excelTemplateResponseMapper.from(source.getExcelTemplates()));
        folderResponse.setChildFolders(shallowMap(source.getChildFolders()));

        return folderResponse;
    }

    public ExcelTemplateFolderResponse mapBaseProperties(ExcelTemplateFolder source) {

        return new ExcelTemplateFolderResponse()
                .setId(source.getId())
                .setParentId(source.getParentFolder() == null ? null : source.getParentFolder().getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setCreated(source.getCreatedDateTime())
                .setModified(source.getModifiedDateTime());
    }

    @Override
    public ExcelTemplateFolderResponse shallowMap(ExcelTemplateFolder source) {

        return mapBaseProperties(source);
    }
}
