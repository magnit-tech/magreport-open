package ru.magnit.magreportbackend.domain.filterinstance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthority;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilterInstanceFolderRolePermissionTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final LocalDateTime CREATED_DATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_DATE_TIME = CREATED_DATE_TIME.plusMinutes(2);
    private static final long FOLDER_ROLE_ID = 2L;
    private static final long AUTH_ID = 3L;

    @BeforeEach
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(5);
    }

    @Test
    void testNoArgsConstructor() {
        var field = getFilterInstanceFolderRolePermission();

        assertEquals(ID, field.getId());
        assertEquals(FOLDER_ROLE_ID, field.getFolderRole().getId());
        assertEquals(AUTH_ID, field.getAuthority().getId());
        assertEquals(CREATED_DATE_TIME, field.getCreatedDateTime());
        assertEquals(MODIFIED_DATE_TIME, field.getModifiedDateTime());


        var testField = new FilterInstanceFolderRolePermission(-ID);
        assertEquals(-ID, testField.getId());
    }

    private static FilterInstanceFolderRolePermission getFilterInstanceFolderRolePermission() {
        return new FilterInstanceFolderRolePermission()
                .setId(ID)
                .setFolderRole(new FilterInstanceFolderRole(FOLDER_ROLE_ID))
                .setAuthority(new FolderAuthority(AUTH_ID))
                .setCreatedDateTime(CREATED_DATE_TIME)
                .setModifiedDateTime(MODIFIED_DATE_TIME);
    }
}