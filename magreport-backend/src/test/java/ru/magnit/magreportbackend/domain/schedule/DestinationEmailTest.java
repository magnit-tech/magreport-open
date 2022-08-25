package ru.magnit.magreportbackend.domain.schedule;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DestinationEmailTest extends BaseEntityTest {

    private static final Long ID = 1L;
    private static final String VALUE = "VALUE";
    private static final LocalDateTime CREATED = LocalDateTime.now();
    private static final LocalDateTime MODIFIED = LocalDateTime.now();


    @BeforeAll
    void init() throws ClassNotFoundException {
        checkNumberOfFields(6);
    }

    @Test
    void testNoArgsConstructor() {

        var destination = getDestination();

        assertEquals(ID, destination.getId());
        assertEquals(VALUE, destination.getValue());
        assertEquals(CREATED, destination.getCreatedDateTime());
        assertEquals(MODIFIED, destination.getModifiedDateTime());
        assertNotNull(destination.getScheduleTask());
    }

    @Test
    void testIdConstructor() {
        var destination = new DestinationEmail(ID);
        assertEquals(ID, destination.getId());
    }

    @Test
    void testValueConstructor() {
        var destination = new DestinationEmail(VALUE);
        assertEquals(VALUE, destination.getValue());
    }

    @Test
    void testEquals() {
        var destination1 = getDestination();
        var destination2 = getDestination();
        assertEquals(destination1, destination2);
    }

    private DestinationEmail getDestination() {
        return new DestinationEmail()
                .setId(ID)
                .setValue(VALUE)
                .setCreatedDateTime(CREATED)
                .setModifiedDateTime(MODIFIED)
                .setScheduleTask(new ScheduleTask());
    }

}
