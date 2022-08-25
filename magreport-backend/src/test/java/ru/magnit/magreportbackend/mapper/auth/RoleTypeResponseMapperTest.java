package ru.magnit.magreportbackend.mapper.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.domain.user.RoleType;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleTypeResponseMapperTest {

    private static final Long ID = 1L;
    private static final String DESCRIPTION = "desc";
    private static final String NAME = "name";
    private static final LocalDateTime CREATE_DATE = LocalDateTime.now().minusDays(5L);
    private static final LocalDateTime MODIFIED_DATE = LocalDateTime.now();
    private static final List<Role> ROLES = Collections.singletonList(new Role());
    @Mock
    private Mapper<RoleResponse, Role> roleResponseMapper;

    @InjectMocks
    private RoleTypeResponseMapper mapper;

    @Test
    void from() {
        final var source = spy(getRoleType());

        when(roleResponseMapper.from(anyList())).thenReturn(getRoleResponses());

        final var result = mapper.from(source);

        assertEquals(ID, result.getId());
        assertEquals(NAME, result.getName());
        assertEquals(DESCRIPTION, result.getDescription());
        assertEquals(CREATE_DATE, result.getCreated());
        assertEquals(MODIFIED_DATE, result.getModified());
        assertEquals(ROLES.size(), result.getRoles().size());

        verify(source).getId();
        verify(source).getName();
        verify(source).getDescription();
        verify(source).getRoles();
        verify(source).getCreatedDateTime();
        verify(source).getModifiedDateTime();
        verify(roleResponseMapper).from(anyList());

        verifyNoMoreInteractions(source, roleResponseMapper);
    }

    private List<RoleResponse> getRoleResponses() {
        return Collections.singletonList(new RoleResponse());
    }

    private RoleType getRoleType() {
        return new RoleType()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreatedDateTime(CREATE_DATE)
                .setModifiedDateTime(MODIFIED_DATE)
                .setRoles(ROLES);
    }

    @Test
    void shallowMap() {
        final var source = spy(getRoleType());

        final var result = mapper.shallowMap(source);

        assertEquals(ID, result.getId());
        assertEquals(NAME, result.getName());
        assertEquals(DESCRIPTION, result.getDescription());
        assertEquals(CREATE_DATE, result.getCreated());
        assertEquals(MODIFIED_DATE, result.getModified());

        verify(source).getId();
        verify(source).getName();
        verify(source).getDescription();
        verify(source).getCreatedDateTime();
        verify(source).getModifiedDateTime();

        verifyNoMoreInteractions(source);
        verifyNoInteractions(roleResponseMapper);
    }
}