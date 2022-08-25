package ru.magnit.magreportbackend.domain.asm;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.user.RoleType;
import ru.magnit.magreportbackend.domain.user.User;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExternalAuthTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "ExternalAuth";
    private static final String DESCRIPTION = "Description for ExternalAuth";
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(8);
    }

    @Test
    void testNoArgsConstructor() {
        var externalAuth = getExternalAuth();
        var externalAuth2 = new ExternalAuth(-ID);

        assertEquals(ID, externalAuth.getId());
        assertEquals(NAME, externalAuth.getName());
        assertEquals(DESCRIPTION, externalAuth.getDescription());
        assertEquals(NOW, externalAuth.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), externalAuth.getModifiedDateTime());
        assertNotNull(externalAuth.getRoleType());
        assertNotNull(externalAuth.getSources());
        assertNotNull(externalAuth.getUser());

        assertEquals(-ID, externalAuth2.getId());
    }

    private static ExternalAuth getExternalAuth() {
        return new ExternalAuth()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setRoleType(new RoleType())
                .setSources(Collections.singletonList(new ExternalAuthSource()))
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW.plusMinutes(2))
                .setUser(new User());
    }
}