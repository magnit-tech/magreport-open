package ru.magnit.magreportbackend.domain.filterinstance;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.user.Role;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FilterInstanceFolderRoleTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final long ROLE_ID = 2L;
    private static final long FOLDER_ID = 3L;
    private static final LocalDateTime CREATED_DATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_DATE_TIME = CREATED_DATE_TIME.plusMinutes(2);

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(6);
    }

    @Test
    void testNoArgsConstructor() {
        var field = getFilterInstanceFolderRole();

        assertEquals(ID, field.getId());
        assertEquals(ROLE_ID, field.getRole().getId());
        assertEquals(FOLDER_ID, field.getFolder().getId());
        assertEquals(CREATED_DATE_TIME, field.getCreatedDateTime());
        assertEquals(MODIFIED_DATE_TIME, field.getModifiedDateTime());

        assertNotNull(field.getPermissions());

        var testField = new FilterInstanceFolderRole(-ID);
        assertEquals(-ID, testField.getId());
    }

    private static FilterInstanceFolderRole getFilterInstanceFolderRole() {
        return new FilterInstanceFolderRole()
                .setId(ID)
                .setRole(new Role(ROLE_ID))
                .setFolder(new FilterInstanceFolder(FOLDER_ID))
                .setPermissions(Collections.emptyList())
                .setCreatedDateTime(CREATED_DATE_TIME)
                .setModifiedDateTime(MODIFIED_DATE_TIME);
    }
}