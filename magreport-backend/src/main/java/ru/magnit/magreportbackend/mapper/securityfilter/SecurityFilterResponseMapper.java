package ru.magnit.magreportbackend.mapper.securityfilter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilter;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceResponseMapper;

import java.util.Collections;
import java.util.LinkedList;

@Service
@RequiredArgsConstructor
public class SecurityFilterResponseMapper implements Mapper<SecurityFilterResponse, SecurityFilter> {

    private final SecurityFilterDataSetResponseMapper dataSetResponseMapper;
    private final FilterInstanceResponseMapper filterInstanceResponseMapper;
    private final RoleSettingsResponseMapper roleSettingsResponseMapper;

    @Override
    public SecurityFilterResponse from(SecurityFilter source) {
        return new SecurityFilterResponse(
            source.getId(),
            filterInstanceResponseMapper.from(source.getFilterInstance()),
            source.getOperationType().getTypeEnum(),
            source.getName(),
            source.getDescription(),
            dataSetResponseMapper.from(Collections.singletonList(source)),
            roleSettingsResponseMapper.from(source.getFilterRoles()),
            source.getUser().getName(),
            source.getCreatedDateTime(),
            source.getModifiedDateTime(),
            new LinkedList<>());
    }
}