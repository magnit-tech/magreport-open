package ru.magnit.magreportbackend.mapper.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import ru.magnit.magreportbackend.domain.user.*;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserViewMapperTest {

    private static final Long ID = 1L;
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String PATRONYMIC = "patronymic";
    private static final String EMAIL = "email";
    private static final UserStatusEnum USER_STATUS_ENUM = UserStatusEnum.ACTIVE;
    private static final UserStatus USER_STATUS = new UserStatus(USER_STATUS_ENUM);


    @Mock
    private ApplicationContext context;

    @InjectMocks
    private UserViewMapper mapper;

    @Test
    void from() {

        final var source = spy(getUser());
        final var result = mapper.from(source);

        assertEquals(ID, result.getId());
        assertEquals(NAME, result.getName());
        assertEquals(DESCRIPTION, result.getDescription());
        assertEquals(FIRST_NAME, result.getFirstName());
        assertEquals(LAST_NAME, result.getLastName());
        assertEquals(PATRONYMIC, result.getPatronymic());
        assertEquals(EMAIL, result.getEmail());
        assertEquals(USER_STATUS_ENUM, result.getStatus());

        verify(source).getId();
        verify(source).getName();
        verify(source).getDescription();
        verify(source).getFirstName();
        verify(source).getLastName();
        verify(source).getPatronymic();
        verify(source).getEmail();
        verify(source).getUserStatus();

        verifyNoMoreInteractions(source);
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
                .setUserStatus(USER_STATUS);
    }
}