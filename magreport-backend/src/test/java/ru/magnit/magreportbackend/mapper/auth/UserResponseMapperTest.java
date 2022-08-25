package ru.magnit.magreportbackend.mapper.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.domain.user.UserRole;
import ru.magnit.magreportbackend.domain.user.UserStatus;
import ru.magnit.magreportbackend.domain.user.UserStatusEnum;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserResponseMapperTest {

    private static final Long ID = 1L;
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String PATRONYMIC = "patronymic";
    private static final String EMAIL = "email";
    private static final UserStatusEnum USER_STATUS_ENUM = UserStatusEnum.ACTIVE;
    private static final UserStatus USER_STATUS = new UserStatus(USER_STATUS_ENUM);
    private static final Role ROLE = new Role();
    private static final UserRole USER_ROLE = new UserRole().setRole(ROLE);
    private static final List<UserRole> USER_ROLES = Collections.singletonList(USER_ROLE);
    private static final LocalDateTime CREATED_DATE = LocalDateTime.now().minusDays(5L);
    private static final LocalDateTime MODIFIED_DATE = LocalDateTime.now();

    @Mock
    private RoleResponseMapper roleResponseMapper;

    @InjectMocks
    private UserResponseMapper mapper;

    @Test
    void from() {

        final var source = spy(getUser());

        when(roleResponseMapper.from(anyList())).thenReturn(Collections.singletonList(new RoleResponse()));

        final var result = mapper.from(source);

        assertEquals(ID, result.getId());
        assertEquals(NAME, result.getName());
        assertEquals(DESCRIPTION, result.getDescription());
        assertEquals(FIRST_NAME, result.getFirstName());
        assertEquals(LAST_NAME, result.getLastName());
        assertEquals(PATRONYMIC, result.getPatronymic());
        assertEquals(EMAIL, result.getEmail());
        assertEquals(USER_STATUS_ENUM, result.getStatus());
        assertEquals(CREATED_DATE, result.getCreated());
        assertEquals(MODIFIED_DATE, result.getModified());
        assertNotNull(result.getRoles());
        assertEquals(1, result.getRoles().size());

        verify(source).getId();
        verify(source).getName();
        verify(source).getDescription();
        verify(source).getFirstName();
        verify(source).getLastName();
        verify(source).getPatronymic();
        verify(source).getEmail();
        verify(source).getUserStatus();
        verify(source).getUserRoles();
        verify(source).getCreatedDateTime();
        verify(source).getModifiedDateTime();

        verifyNoMoreInteractions(source, roleResponseMapper);

    }

    private User getUser() {
        return new User()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setFirstName(FIRST_NAME)
                .setLastName(LAST_NAME)
                .setPatronymic(PATRONYMIC)
                .setEmail(EMAIL)
                .setUserStatus(USER_STATUS)
                .setUserRoles(USER_ROLES)
                .setCreatedDateTime(CREATED_DATE)
                .setModifiedDateTime(MODIFIED_DATE);
    }
}