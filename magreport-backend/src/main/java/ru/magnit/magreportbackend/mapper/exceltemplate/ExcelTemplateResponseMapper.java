package ru.magnit.magreportbackend.mapper.exceltemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplate;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ExcelTemplateResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ExcelTemplateResponseMapper implements Mapper<ExcelTemplateResponse, ExcelTemplate> {

    @Override
    public ExcelTemplateResponse from(ExcelTemplate source) {

        return mapBaseProperties(source);
    }

    private ExcelTemplateResponse mapBaseProperties(ExcelTemplate source) {

        return new ExcelTemplateResponse()
                .setId(source.getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setUserName(source.getUser().getName())
                .setCreated(source.getCreatedDateTime())
                .setModified(source.getModifiedDateTime());

    }
}
