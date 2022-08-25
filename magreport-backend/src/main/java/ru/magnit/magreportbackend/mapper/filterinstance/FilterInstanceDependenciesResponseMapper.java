package ru.magnit.magreportbackend.mapper.filterinstance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstance;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceDependenciesResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.auth.UserResponseMapper;
import ru.magnit.magreportbackend.mapper.filterreport.FilterReportResponseMapper;
import ru.magnit.magreportbackend.mapper.filtertemplate.FilterTemplateResponseMapper;

import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilterInstanceDependenciesResponseMapper implements Mapper<FilterInstanceDependenciesResponse, FilterInstance> {

    private final UserResponseMapper userResponseMapper;
    private final FilterTemplateResponseMapper filterTemplateResponseMapper;
    private final FilterInstanceFieldResponseMapper filterInstanceFieldResponseMapper;
    private final FilterReportResponseMapper filterReportResponseMapper;

    @Override
    public FilterInstanceDependenciesResponse from(FilterInstance source) {
        return new FilterInstanceDependenciesResponse(
                source.getId(),
                source.getName(),
                source.getDescription(),
                userResponseMapper.from(source.getUser()),
                FilterTypeEnum.getByOrdinal(source.getFilterTemplate().getType().getId()),
                source.getFilterTemplate().getOperations().stream().map(oper -> FilterOperationTypeEnum.getById(oper.getType().getId())).collect(Collectors.toList()),
                filterTemplateResponseMapper.from(source.getFilterTemplate()),
                filterInstanceFieldResponseMapper.from(source.getFields()),
                filterReportResponseMapper.from(source.getFilterReports()),
                source.getDataSet() == null || source.getDataSet().getFields().stream().anyMatch(field -> !field.getIsSync()),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
        );
    }
}
