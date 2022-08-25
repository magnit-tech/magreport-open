package ru.magnit.magreportbackend.domain.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RoleTypeTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "Test type name";
    private static final String DESCRIPTION = "Test type description";
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(6);
    }

    @Test
    void testNoArgsConstructor() {
        var type = getRole();

        assertEquals(ID, type.getId());
        assertEquals(NAME, type.getName());
        assertEquals(DESCRIPTION, type.getDescription());

        assertEquals(NOW, type.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), type.getModifiedDateTime());
        assertNotNull(type.getRoles());

        var testingType = new Role(-ID);
        assertEquals(-ID, testingType.getId());
    }

    private static RoleType getRole() {

        return new RoleType()
            .setId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setCreatedDateTime(NOW)
            .setModifiedDateTime(NOW.plusMinutes(2))
            .setRoles(Collections.singletonList(new Role()));
    }
}