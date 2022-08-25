package ru.magnit.magreportbackend.mapper.asm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.asm.ExternalAuth;
import ru.magnit.magreportbackend.domain.user.RoleType;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.mapper.auth.RoleTypeResponseMapper;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)
class AsmSecurityResponseMapperTest {

    private static final String EXTERNAL_AUTH_NAME = "External Auth";
    private static final String EXTERNAL_AUTH_DESCRIPTION = "External Auth for test";
    private static final LocalDateTime EXTERNAL_AUTH_CREATED_DATETIME
            = LocalDateTime.of(2020, 1, 1, 12, 34);
    private static final LocalDateTime EXTERNAL_AUTH_MODIFIED_DATETIME
            = LocalDateTime.of(2020, 1, 1, 12, 34);

    @Mock
    private AsmSecuritySourceResponseMapper sourceMapper;

    @Mock
    private RoleTypeResponseMapper roleTypeResponseMapper;

    @InjectMocks
    private AsmSecurityResponseMapper mapper;

    @Test
    void from() {
        final var id = 1L;
        final var result = mapper.from(getExternalAuth(id));

        assertEquals(id, result.id());
        assertEquals(EXTERNAL_AUTH_NAME, result.name());
        assertEquals(EXTERNAL_AUTH_DESCRIPTION, result.description());
        assertEquals(EXTERNAL_AUTH_CREATED_DATETIME, result.created());
        assertEquals(EXTERNAL_AUTH_MODIFIED_DATETIME, result.modified());
        assertNotNull(result.sources());
        assertEquals(0, result.sources().size());
    }

    @Test
    void listFrom() {
        final var firstId = 1L;
        final var secondId = 2L;

        final var externalAuthList = Arrays.asList(
                getExternalAuth(firstId),
                getExternalAuth(secondId)
        );

        final var result = mapper.from(externalAuthList);

        assertEquals(externalAuthList.size(), result.size());
        assertEquals(firstId, result.get(0).id());
        assertEquals(secondId, result.get(1).id());
    }

    private ExternalAuth getExternalAuth(Long id) {
        return new ExternalAuth(id)
                .setName(EXTERNAL_AUTH_NAME)
                .setDescription(EXTERNAL_AUTH_DESCRIPTION)
                .setCreatedDateTime(EXTERNAL_AUTH_CREATED_DATETIME)
                .setModifiedDateTime(EXTERNAL_AUTH_MODIFIED_DATETIME)
                .setUser(new User())
                .setRoleType(new RoleType(1L));
    }
}