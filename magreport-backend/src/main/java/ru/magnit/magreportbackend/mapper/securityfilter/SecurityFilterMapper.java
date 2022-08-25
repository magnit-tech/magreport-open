package ru.magnit.magreportbackend.mapper.securityfilter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstance;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationType;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilter;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterFolder;
import ru.magnit.magreportbackend.dto.request.securityfilter.FieldMapping;
import ru.magnit.magreportbackend.dto.request.securityfilter.SecurityFilterAddRequest;
import ru.magnit.magreportbackend.dto.request.securityfilter.SecurityFilterDataSetAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SecurityFilterMapper implements Mapper<SecurityFilter, SecurityFilterAddRequest> {

    private final SecurityFilterDataSetFieldMapper fieldMapper;
    private final SecurityFilterDataSetMapper dataSetMapper;

    @Override
    public SecurityFilter from(SecurityFilterAddRequest source) {
        final var securityFilter = new SecurityFilter()
            .setOperationType(new FilterOperationType(source.getOperationType()))
            .setName(source.getName())
            .setDescription(source.getDescription())
            .setFolder(new SecurityFilterFolder(source.getFolderId()))
            .setFilterInstance(new FilterInstance(source.getFilterInstanceId()))
            .setDataSets(dataSetMapper.from(source.getDataSets()))
            .setFieldMappings(fieldMapper.from(getFieldMappings(source.getDataSets())));

        securityFilter.getDataSets().forEach(dataSet -> dataSet.setSecurityFilter(securityFilter));
        securityFilter.getFieldMappings().forEach(field -> field.setSecurityFilter(securityFilter));

        return securityFilter;
    }

    private List<FieldMapping> getFieldMappings(List<SecurityFilterDataSetAddRequest> source) {

        return source
            .stream()
            .flatMap(request -> request.getFields().stream())
            .collect(Collectors.toList());
    }
}
