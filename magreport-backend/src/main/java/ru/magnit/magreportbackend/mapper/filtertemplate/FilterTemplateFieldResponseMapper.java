package ru.magnit.magreportbackend.mapper.filtertemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldType;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateField;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTemplateFieldResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FilterTemplateFieldResponseMapper implements Mapper<FilterTemplateFieldResponse, FilterTemplateField> {

    @Override
    public FilterTemplateFieldResponse from(FilterTemplateField source) {

        return mapBaseProperties(source);
    }

    private FilterTemplateFieldResponse mapBaseProperties(FilterTemplateField source) {

        return new FilterTemplateFieldResponse()
                .setId(source.getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setType(mapFilterFieldType(source.getType()))
                .setCreated(source.getCreatedDateTime())
                .setModified(source.getModifiedDateTime());
    }

    private FilterFieldTypeEnum mapFilterFieldType(FilterFieldType source) {
        return FilterFieldTypeEnum.values()[source.getId().intValue()];
    }
}
