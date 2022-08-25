package ru.magnit.magreportbackend.mapper.filterinstance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstance;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceFolder;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplate;
import ru.magnit.magreportbackend.dto.request.filterinstance.FilterInstanceAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FilterInstanceMapper implements Mapper<FilterInstance, FilterInstanceAddRequest> {

    private final FilterInstanceFieldMapper fieldMapper;

    @Override
    public FilterInstance from(FilterInstanceAddRequest source) {
        var filterInstance = mapBaseProperties(source);
        filterInstance.setFields(fieldMapper.from(source.getFields()));
        filterInstance.getFields().forEach(field -> field.setInstance(filterInstance));

        return filterInstance;
    }

    private FilterInstance mapBaseProperties(FilterInstanceAddRequest source) {
        return new FilterInstance()
                .setName(source.getName())
                .setCode(source.getCode() == null ? "" : source.getCode())
                .setDescription(source.getDescription())
                .setFolder(new FilterInstanceFolder(source.getFolderId()))
                .setDataSet(source.getDataSetId() == null ? null : new DataSet(source.getDataSetId()))
                .setFilterTemplate(new FilterTemplate(source.getTemplateId()));
    }
}
