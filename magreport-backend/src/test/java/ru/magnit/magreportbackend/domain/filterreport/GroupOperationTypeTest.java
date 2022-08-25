package ru.magnit.magreportbackend.domain.filterreport;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GroupOperationTypeTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "Test folder";
    private static final String DESCRIPTION = "Folder description";
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(6);
    }

    @Test
    void testNoArgsConstructor() {
        var type = getType();

        assertEquals(ID, type.getId());
        assertEquals(NAME, type.getName());
        assertEquals(DESCRIPTION, type.getDescription());
        assertEquals(NOW, type.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), type.getModifiedDateTime());

        assertNotNull(type.getGroups());

        var testType = new GroupOperationType(-ID);
        assertEquals(-ID, testType.getId());
    }

    private static GroupOperationType getType() {
        return new GroupOperationType()
            .setId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setCreatedDateTime(NOW)
            .setModifiedDateTime(NOW.plusMinutes(2))
            .setGroups(Collections.singletonList(new FilterReportGroup()));
    }
}