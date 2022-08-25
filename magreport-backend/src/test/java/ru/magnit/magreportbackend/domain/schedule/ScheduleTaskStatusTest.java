package ru.magnit.magreportbackend.domain.schedule;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ScheduleTaskStatusTest extends BaseEntityTest {

    private final static Long ID = 0L;
    private final static String NAME = "name";
    private final static String DESCRIPTION = "description";
    private final static LocalDateTime CREATED = LocalDateTime.now();
    private final static LocalDateTime MODIFIED = LocalDateTime.now().plusDays(2);

    @BeforeAll
    void init() throws ClassNotFoundException {
        checkNumberOfFields(6);
    }

    @Test
    void testNoArgs(){
        var response = getScheduleTaskStatus();

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CREATED, response.getCreatedDateTime());
        assertEquals(MODIFIED, response.getModifiedDateTime());
        assertNotNull(response.getScheduleTasks());
    }

    @Test
    void testIdArgs() {
        var response = new ScheduleTaskStatus(ID);
        assertEquals(ID, response.getId());
    }

    @Test
    void testEquals() {
            var item1 = getScheduleTaskStatus();
            var item2 = getScheduleTaskStatus();
            assertEquals(item1, item2);
        }

    private ScheduleTaskStatus getScheduleTaskStatus() {
        return new ScheduleTaskStatus()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreatedDateTime(CREATED)
                .setModifiedDateTime(MODIFIED)
                .setScheduleTasks(Collections.emptyList());
    }
}
