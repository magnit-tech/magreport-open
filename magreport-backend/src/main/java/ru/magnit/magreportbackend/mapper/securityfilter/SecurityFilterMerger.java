package ru.magnit.magreportbackend.mapper.securityfilter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationType;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilter;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterFolder;
import ru.magnit.magreportbackend.dto.request.securityfilter.FieldMapping;
import ru.magnit.magreportbackend.dto.request.securityfilter.SecurityFilterAddRequest;
import ru.magnit.magreportbackend.dto.request.securityfilter.SecurityFilterDataSetAddRequest;
import ru.magnit.magreportbackend.mapper.Merger;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SecurityFilterMerger implements Merger<SecurityFilter, SecurityFilterAddRequest> {

    private final SecurityFilterDataSetFieldMapper fieldMapper;
    private final SecurityFilterDataSetMapper dataSetMapper;

    @Override
    public SecurityFilter merge(SecurityFilter target, SecurityFilterAddRequest source) {
        target
            .setOperationType(new FilterOperationType(source.getOperationType()))
            .setName(source.getName())
            .setDescription(source.getDescription())
            .setFolder(new SecurityFilterFolder(source.getFolderId()))
            .setDataSets(dataSetMapper.from(source.getDataSets()))
            .setFieldMappings(fieldMapper.from(getFieldMappings(source.getDataSets())));

        target.getDataSets().forEach(dataSet -> dataSet.setSecurityFilter(target));
        target.getFieldMappings().forEach(field -> field.setSecurityFilter(target));

        return target;
    }

    private List<FieldMapping> getFieldMappings(List<SecurityFilterDataSetAddRequest> source) {

        return source
            .stream()
            .flatMap(request -> request.getFields().stream())
            .collect(Collectors.toList());
    }
}
