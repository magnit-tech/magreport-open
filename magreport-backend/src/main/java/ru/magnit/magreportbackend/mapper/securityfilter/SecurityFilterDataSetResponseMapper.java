package ru.magnit.magreportbackend.mapper.securityfilter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilter;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterDataSetField;
import ru.magnit.magreportbackend.dto.response.securityfilter.FieldMappingResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterDataSetResponse;
import ru.magnit.magreportbackend.exception.NotImplementedException;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetResponseMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SecurityFilterDataSetResponseMapper implements Mapper<SecurityFilterDataSetResponse, SecurityFilter> {

    private final DataSetResponseMapper dataSetResponseMapper;

    @Override
    public SecurityFilterDataSetResponse from(SecurityFilter source) {
        throw new NotImplementedException("This mapper is forbidden");
    }

    @Override
    public List<SecurityFilterDataSetResponse> from(List<SecurityFilter> source) {
        return source
            .stream()
            .flatMap(o -> o.getDataSets().stream())
            .map(o -> new SecurityFilterDataSetResponse(
                dataSetResponseMapper.from(o.getDataSet()),
                getMappings(o.getDataSet().getId(), o.getSecurityFilter().getFieldMappings())
            ))
            .collect(Collectors.toList());
    }

    private List<FieldMappingResponse> getMappings(Long dataSetId, List<SecurityFilterDataSetField> fieldMappings) {

        return fieldMappings
            .stream()
            .filter(o -> o.getDataSetField().getDataSet().getId().equals(dataSetId))
            .map(o -> new FieldMappingResponse(
                o.getFilterInstanceField().getId(),
                o.getDataSetField().getId()
            ))
            .collect(Collectors.toList());
    }
}
