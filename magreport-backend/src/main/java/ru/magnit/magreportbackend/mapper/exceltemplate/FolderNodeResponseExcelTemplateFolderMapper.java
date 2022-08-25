package ru.magnit.magreportbackend.mapper.exceltemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplateFolder;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FolderNodeResponseExcelTemplateFolderMapper  implements Mapper<FolderNodeResponse, ExcelTemplateFolder> {
    @Override
    public FolderNodeResponse from(ExcelTemplateFolder source) {
        return new FolderNodeResponse(
                source.getId(),
                source.getParentFolder() != null ? source.getParentFolder().getId() : null,
                source.getName(),
                source.getDescription(),
                source.getCreatedDateTime(),
                source.getModifiedDateTime()
        );
    }
}
