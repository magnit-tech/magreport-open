package ru.magnit.magreportbackend.mapper.securityfilter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilter;
import ru.magnit.magreportbackend.dto.response.securityfilter.RoleSettingsResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class SecurityFilterRoleSettingsResponseMapperTest {

    @Mock
    private RoleSettingsResponseMapper roleSettingsResponseMapper;

    @InjectMocks
    private SecurityFilterRoleSettingsResponseMapper mapper;

    private final static Long ID = 1L;

    @Test
    void from() {

        Mockito.when(roleSettingsResponseMapper.from(Mockito.anyList())).thenReturn(Collections.singletonList(getRoleSettingsResponse()));

        var response = mapper.from(getSecurityFilter());

        Assertions.assertEquals(ID, response.securityFilterId());
        Assertions.assertNotNull(response.roleSettings());

        Mockito.verifyNoMoreInteractions(roleSettingsResponseMapper);
    }

    @Test
    void fromList() {
        Mockito.when(roleSettingsResponseMapper.from(Mockito.anyList())).thenReturn(Collections.singletonList(getRoleSettingsResponse()));

        var responses = mapper.from(Collections.singletonList(getSecurityFilter()));
        Assertions.assertEquals(1, responses.size());

        var response = responses.get(0);

        Assertions.assertEquals(ID, response.securityFilterId());
        Assertions.assertNotNull(response.roleSettings());

        Mockito.verifyNoMoreInteractions(roleSettingsResponseMapper);
    }


    private SecurityFilter getSecurityFilter() {
        return new SecurityFilter()
                .setId(ID)
                .setFilterRoles(Collections.emptyList());
    }

    private RoleSettingsResponse getRoleSettingsResponse() {
        return new RoleSettingsResponse(
                new RoleResponse(),
                Collections.emptyList());
    }

}
