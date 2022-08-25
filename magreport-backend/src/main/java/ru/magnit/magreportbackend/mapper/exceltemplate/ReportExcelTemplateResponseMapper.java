package ru.magnit.magreportbackend.mapper.exceltemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.excel.ReportExcelTemplate;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ReportExcelTemplateResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ReportExcelTemplateResponseMapper implements Mapper<ReportExcelTemplateResponse, ReportExcelTemplate> {
    @Override
    public ReportExcelTemplateResponse from(ReportExcelTemplate source) {
        return new ReportExcelTemplateResponse()
                .setExcelTemplateId(source.getExcelTemplate().getId())
                .setName(source.getExcelTemplate().getName())
                .setDescription(source.getExcelTemplate().getDescription())
                .setIsDefault(source.getIsDefault());

    }
}
