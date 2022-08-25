package ru.magnit.magreportbackend.service.jobengine.filter.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.dto.inner.filter.FilterChildNodesRequestData;
import ru.magnit.magreportbackend.dto.inner.filter.FilterRequestData;
import ru.magnit.magreportbackend.dto.inner.filter.FilterValueListRequestData;
import ru.magnit.magreportbackend.service.domain.TemplateParserService;
import ru.magnit.magreportbackend.service.jobengine.filter.FilterQueryBuilder;

@Service
@RequiredArgsConstructor
public class H2FilterQueryBuilder implements FilterQueryBuilder {

    private final TemplateParserService templateParserService;

    @Value("${magreport.query-templates.filter.value-list.h2}")
    private String valueListTemplatePath;

    @Value("${magreport.query-templates.filter.child-nodes.h2}")
    private String childNodesTemplatePath;

    @Value("${magreport.query-templates.filter.fields.h2}")
    private String fieldValuesTemplatePath;

    @Override
    public String getFilterValuesQuery(FilterValueListRequestData requestData) {
        return templateParserService.parseTemplate(valueListTemplatePath, requestData);
    }

    @Override
    public String getChildNodesQuery(FilterChildNodesRequestData requestData) {
        return templateParserService.parseTemplate(childNodesTemplatePath, requestData);
    }

    @Override
    public String getFilterFieldsQuery(FilterRequestData requestData) {

        return templateParserService.parseTemplate(fieldValuesTemplatePath, requestData);
    }
}
