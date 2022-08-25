package ru.magnit.magreportbackend.mapper.filterinstance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateField;
import ru.magnit.magreportbackend.dto.request.filterinstance.FilterInstanceFieldAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FilterInstanceFieldMapper implements Mapper<FilterInstanceField, FilterInstanceFieldAddRequest> {

    @Override
    public FilterInstanceField from(FilterInstanceFieldAddRequest source) {
        return mapBaseProperties(source);
    }

    private FilterInstanceField mapBaseProperties(FilterInstanceFieldAddRequest source) {
        return new FilterInstanceField()
                .setLevel(source.getLevel())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setDataSetField(source.getDataSetFieldId() == null ? null : new DataSetField(source.getDataSetFieldId()))
                .setTemplateField(new FilterTemplateField(source.getTemplateFieldId()))
                .setExpand(source.getExpand() != null && source.getExpand());
    }
}
