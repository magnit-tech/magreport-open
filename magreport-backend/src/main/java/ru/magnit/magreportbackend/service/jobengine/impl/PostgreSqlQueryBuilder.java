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
public class PostgreSqlQueryBuilder implements QueryBuilder {

    private final TemplateParserService templateParserService;

    @Value("${magreport.query-templates.postgresql}")
    private String templatePath;

    @Override
    public String getQuery(ReportJobData reportData) {
        if (PROCEDURE.equalsIsLong(reportData.dataSetTypeId())) {
            String nameProcedure = reportData.reportData().schemaName() + "." + reportData.reportData().tableName();
            return String.format("CALL %s (%s)", nameProcedure, reportData.id());
        }

        return templateParserService.parseTemplate(templatePath, reportData);
    }
}
