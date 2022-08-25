package ru.magnit.magreportbackend.domain.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserStatusTest extends BaseEntityTest {
    private static final long ID = 1L;
    private static final LocalDateTime CREATED_DATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_DATE_TIME = CREATED_DATE_TIME.plusMinutes(2);
    private static final String NAME = "Test name";
    private static final String DESCRIPTION = "Test description";

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(6);
    }

    @Test
    void testNoArgsConstructor() {
        var objectUnderTest = getUserStatus();

        assertEquals(ID, objectUnderTest.getId());
        assertEquals(NAME, objectUnderTest.getName());
        assertEquals(DESCRIPTION, objectUnderTest.getDescription());
        assertEquals(CREATED_DATE_TIME, objectUnderTest.getCreatedDateTime());
        assertEquals(MODIFIED_DATE_TIME, objectUnderTest.getModifiedDateTime());

        assertNotNull(objectUnderTest.getUsers());

        var testObject = new UserStatus(-ID);
        assertEquals(-ID, testObject.getId());
    }

    private UserStatus getUserStatus() {

        return new UserStatus()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setUsers(Collections.singletonList(new User()))

                .setCreatedDateTime(CREATED_DATE_TIME)
                .setModifiedDateTime(MODIFIED_DATE_TIME);
    }
}