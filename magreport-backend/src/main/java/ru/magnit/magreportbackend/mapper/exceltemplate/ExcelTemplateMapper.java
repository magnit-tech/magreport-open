package ru.magnit.magreportbackend.mapper.exceltemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplate;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplateFolder;
import ru.magnit.magreportbackend.dto.request.exceltemplate.ExcelTemplateAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ExcelTemplateMapper implements Mapper<ExcelTemplate, ExcelTemplateAddRequest> {

    @Override
    public ExcelTemplate from(ExcelTemplateAddRequest source) {

        return mapBaseProperties(source);
    }

    private ExcelTemplate mapBaseProperties(ExcelTemplateAddRequest source) {

        return new ExcelTemplate()
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setFolder(new ExcelTemplateFolder(source.getFolderId()));
    }
}
