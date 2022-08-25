package ru.magnit.magreportbackend.mapper.filterinstance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceFieldResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FilterInstanceFieldResponseMapper implements Mapper<FilterInstanceFieldResponse, FilterInstanceField> {

    @Override
    public FilterInstanceFieldResponse from(FilterInstanceField source) {

        return mapBaseProperties(source);
    }

    private FilterInstanceFieldResponse mapBaseProperties(FilterInstanceField source) {

        return new FilterInstanceFieldResponse()
                .setId(source.getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setLevel(source.getLevel())
                .setType(mapFieldType(source.getTemplateField().getType().getId()))
                .setTemplateFieldId(source.getTemplateField().getId())
                .setDataSetFieldId(nvl(source.getDataSetField()))
                .setCreated(source.getCreatedDateTime())
                .setModified(source.getModifiedDateTime())
                .setExpand(source.getExpand());
    }

    private FilterFieldTypeEnum mapFieldType(Long id) {
        return FilterFieldTypeEnum.values()[id.intValue()];
    }

    private Long nvl(DataSetField source) {
        return source == null ? null : source.getId();
    }
}
