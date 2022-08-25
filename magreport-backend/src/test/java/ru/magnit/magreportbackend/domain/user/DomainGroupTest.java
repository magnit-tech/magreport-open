package ru.magnit.magreportbackend.domain.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DomainGroupTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "Test domainGroup";
    private static final String DESCRIPTION = "Test domainGroup description";
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(6);
    }

    @Test
    void testNoArgsConstructor() {
        var domainGroup = getDomainGroup();

        assertEquals(ID, domainGroup.getId());
        assertEquals(NAME, domainGroup.getName());
        assertEquals(DESCRIPTION, domainGroup.getDescription());

        assertEquals(NOW, domainGroup.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), domainGroup.getModifiedDateTime());
        assertNotNull(domainGroup.getRoleDomainGroups());

        var testingGroup = new DomainGroup(-ID);
        assertEquals(-ID, testingGroup.getId());
    }

    private static DomainGroup getDomainGroup() {
        return new DomainGroup()
            .setId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setCreatedDateTime(NOW)
            .setModifiedDateTime(NOW.plusMinutes(2))
            .setRoleDomainGroups(Collections.singletonList(new RoleDomainGroup()));
    }
}