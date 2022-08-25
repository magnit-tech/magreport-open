package ru.magnit.magreportbackend.mapper.filtertemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplate;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTemplateResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilterTemplateResponseMapper implements Mapper<FilterTemplateResponse, FilterTemplate> {

    private final FilterTypeResponseMapper filterTypeResponseMapper;
    private final FilterTemplateFieldResponseMapper fieldMapper;

    @Override
    public FilterTemplateResponse from(FilterTemplate source) {
        var response = mapBaseProperties(source);

        response
                .setType(filterTypeResponseMapper.from(source.getType()))
                .setFields(fieldMapper.from(source.getFields()))
                .setSupportedOperations(mapOperationTypes(source));

        return response;
    }

    private List<FilterOperationTypeEnum> mapOperationTypes(FilterTemplate source) {
        return source.getOperations()
                .stream()
                .map(oper-> FilterOperationTypeEnum.values()[oper.getType().getId().intValue()])
                .collect(Collectors.toList());
    }

    private FilterTemplateResponse mapBaseProperties(FilterTemplate source) {

        return new FilterTemplateResponse()
                .setId(source.getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setUserName(source.getUser().getName())
                .setCreated(source.getCreatedDateTime())
                .setModified(source.getModifiedDateTime());
    }
}
