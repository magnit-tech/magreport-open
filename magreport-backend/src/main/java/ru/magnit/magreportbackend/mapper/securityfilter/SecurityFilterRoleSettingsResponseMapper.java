package ru.magnit.magreportbackend.mapper.securityfilter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilter;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterRoleSettingsResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class SecurityFilterRoleSettingsResponseMapper implements Mapper<SecurityFilterRoleSettingsResponse, SecurityFilter> {

    private final RoleSettingsResponseMapper roleResponseMapper;

    @Override
    public SecurityFilterRoleSettingsResponse from(SecurityFilter source) {
        return new SecurityFilterRoleSettingsResponse(
            source.getId(),
            roleResponseMapper.from(source.getFilterRoles())
        );
    }
}
