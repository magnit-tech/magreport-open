package ru.magnit.magreportbackend.mapper.securityfilter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterRole;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterRoleTuple;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;
import ru.magnit.magreportbackend.dto.tuple.Tuple;
import ru.magnit.magreportbackend.mapper.auth.RoleResponseMapper;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class RoleSettingsResponseMapperTest {

    @Mock
    private RoleResponseMapper roleResponseMapper;

    @Mock
    private SecurityFilterRoleTupleResponseMapper tupleResponseMapper;

    @InjectMocks
    private RoleSettingsResponseMapper mapper;

    @Test
    void from() {
        final var source = spy(getSecurityFilterRole());

        when(roleResponseMapper.from(any(Role.class))).thenReturn(new RoleResponse());
        when(tupleResponseMapper.from(anyList())).thenReturn(Collections.singletonList(new Tuple()));

        final var result = mapper.from(source);

        assertNotNull(result.role());
        assertNotNull(result.tuples());
        assertEquals(1, result.tuples().size());


        verify(source).getRole();
        verify(source).getFilterRoleTuples();
        verify(roleResponseMapper).from(any(Role.class));
        verify(tupleResponseMapper).from(anyList());
        verifyNoMoreInteractions(source, roleResponseMapper, tupleResponseMapper);
    }

    private SecurityFilterRole getSecurityFilterRole() {
        return new SecurityFilterRole()
                .setRole(new Role())
                .setFilterRoleTuples(Collections.singletonList(new SecurityFilterRoleTuple()));
    }
}