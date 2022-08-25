package ru.magnit.magreportbackend.mapper.filterinstance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstance;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.filtertemplate.FilterTypeResponseMapper;

@Service
@RequiredArgsConstructor
public class FilterInstanceResponseMapper implements Mapper<FilterInstanceResponse, FilterInstance> {

    private final FilterInstanceFieldResponseMapper fieldMapper;
    private final FilterTypeResponseMapper filterTypeResponseMapper;

    @Override
    public FilterInstanceResponse from(FilterInstance source) {
        var response = mapBaseProperties(source);

        response.setFields(fieldMapper.from(source.getFields()));

        return response;
    }

    private FilterInstanceResponse mapBaseProperties(FilterInstance source) {
        return new FilterInstanceResponse()
                .setId(source.getId())
                .setFolderId(source.getFolder().getId())
                .setTemplateId(source.getFilterTemplate().getId())
                .setType(filterTypeResponseMapper.from(source.getFilterTemplate().getType()))
                .setDataSetId(source.getDataSet() == null ? null : source.getDataSet().getId())
                .setName(source.getName())
                .setCode(source.getCode())
                .setDescription(source.getDescription())
                .setUserName(source.getUser().getName())
                .setCreated(source.getCreatedDateTime())
                .setModified(source.getModifiedDateTime());
    }
}
