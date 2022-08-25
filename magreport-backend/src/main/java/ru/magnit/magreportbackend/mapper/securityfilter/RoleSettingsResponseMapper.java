package ru.magnit.magreportbackend.mapper.securityfilter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterRole;
import ru.magnit.magreportbackend.dto.response.securityfilter.RoleSettingsResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.auth.RoleResponseMapper;

@Service
@RequiredArgsConstructor
public class RoleSettingsResponseMapper implements Mapper<RoleSettingsResponse, SecurityFilterRole> {

    private final RoleResponseMapper roleResponseMapper;
    private final SecurityFilterRoleTupleResponseMapper tupleResponseMapper;

    @Override
    public RoleSettingsResponse from(SecurityFilterRole source) {
        return new RoleSettingsResponse(
            roleResponseMapper.from(source.getRole()),
            tupleResponseMapper.from(source.getFilterRoleTuples()));
    }
}
