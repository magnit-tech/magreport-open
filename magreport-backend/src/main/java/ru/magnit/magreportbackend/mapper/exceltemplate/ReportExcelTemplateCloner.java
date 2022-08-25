package ru.magnit.magreportbackend.mapper.exceltemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.excel.ReportExcelTemplate;
import ru.magnit.magreportbackend.mapper.Cloner;
@Service
@RequiredArgsConstructor
public class ReportExcelTemplateCloner implements Cloner<ReportExcelTemplate> {
    @Override
    public ReportExcelTemplate clone(ReportExcelTemplate source) {
        return new ReportExcelTemplate()
                .setIsDefault(source.getIsDefault())
                .setExcelTemplate(source.getExcelTemplate());
    }
}
