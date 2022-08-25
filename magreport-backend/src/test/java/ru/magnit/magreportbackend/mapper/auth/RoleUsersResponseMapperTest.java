package ru.magnit.magreportbackend.mapper.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.domain.user.UserRole;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleUsersResponseMapperTest {

    private static final Long ID = 1L;
    private static final User USER = new User();
    private static final UserRole USER_ROLE = spy(new UserRole().setUser(USER));
    private static final List<UserRole> USER_ROLES = Collections.singletonList(USER_ROLE);
    @Mock
    private Mapper<UserResponse, User> userResponseMapper;

    @InjectMocks
    private RoleUsersResponseMapper mapper;

    @Test
    void from() {

        final var source = spy(getRole());

        when(userResponseMapper.from(anyList())).thenReturn(Collections.singletonList(new UserResponse()));

        final var result = mapper.from(source);

        assertEquals(ID, result.getId());
        assertNotNull(result.getUsers());
        assertEquals(1, result.getUsers().size());

        verify(source).getId();
        verify(source).getUserRoles();
        verify(USER_ROLE).getUser();

        verifyNoMoreInteractions(userResponseMapper, source);
    }

    private Role getRole() {
        return new Role()
                .setId(ID)
                .setUserRoles(USER_ROLES);
    }
}