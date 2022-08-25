package ru.magnit.magreportbackend.service.jobengine.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobData;
import ru.magnit.magreportbackend.service.domain.TemplateParserService;
import ru.magnit.magreportbackend.service.jobengine.QueryBuilder;

import static ru.magnit.magreportbackend.domain.dataset.DataSetTypeEnum.PROCEDURE;

@Service
@RequiredArgsConstructor
public class H2QueryBuilder implements QueryBuilder {

    private final TemplateParserService templateParserService;

    @Value("${magreport.query-templates.h2}")
    private String templatePath;

    @Override
    public String getQuery(ReportJobData reportData) {

        if (PROCEDURE.equalsIsLong(reportData.dataSetTypeId()))
            return "";
        else
            return templateParserService.parseTemplate(templatePath, reportData);
    }
}
