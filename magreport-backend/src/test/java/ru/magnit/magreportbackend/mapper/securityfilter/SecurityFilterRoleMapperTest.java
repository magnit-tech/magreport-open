package ru.magnit.magreportbackend.mapper.securityfilter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterRoleTuple;
import ru.magnit.magreportbackend.dto.request.securityfilter.RoleSettingsRequest;
import ru.magnit.magreportbackend.dto.request.securityfilter.SecurityFilterSetRoleRequest;
import ru.magnit.magreportbackend.exception.NotImplementedException;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class SecurityFilterRoleMapperTest {

    @Mock
    private SecurityFilterRoleTupleMapper securityFilterRoleTupleMapper;

    @InjectMocks
    private SecurityFilterRoleMapper mapper;

    private final static Long ID = 1L;

    @Test
    void from() {
        var request = new SecurityFilterSetRoleRequest();
        Assertions.assertThrows(NotImplementedException.class, () -> mapper.from(request));
    }

    @Test
    void fromList() {
        Mockito.when(securityFilterRoleTupleMapper.from(Mockito.anyList())).thenReturn(Collections.singletonList(new SecurityFilterRoleTuple()));

        var responses = mapper.from(Collections.singletonList(getSecurityFilterSetRoleRequest()));

        Assertions.assertEquals(1, responses.size());
        var response = responses.get(0);

        Assertions.assertNotNull(response.getSecurityFilter());
        Assertions.assertNotNull(response.getRole());
        Assertions.assertEquals(1, response.getFilterRoleTuples().size());

        Mockito.verifyNoMoreInteractions(securityFilterRoleTupleMapper);
    }


    private SecurityFilterSetRoleRequest getSecurityFilterSetRoleRequest() {
        return new SecurityFilterSetRoleRequest()
                .setSecurityFilterId(ID)
                .setRoleSettings(Collections.singletonList(
                        new RoleSettingsRequest()
                                .setRoleId(ID)
                                .setTuples(Collections.emptyList())
                ));
    }

}
