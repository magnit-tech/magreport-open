package ru.magnit.magreportbackend.mapper.exceltemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplateFolder;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ExcelTemplateFolderMapper implements Mapper<ExcelTemplateFolder, FolderAddRequest> {

    @Override
    public ExcelTemplateFolder from(FolderAddRequest source) {
        return mapBaseProperties(source);
    }

    private ExcelTemplateFolder mapBaseProperties(FolderAddRequest source) {

        return new ExcelTemplateFolder()
                .setParentFolder(source.getParentId() == null ? null : new ExcelTemplateFolder(source.getParentId()))
                .setName(source.getName())
                .setDescription(source.getDescription());
    }
}
