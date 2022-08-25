package ru.magnit.magreportbackend.domain.reportjob;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReportJobStateTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "Test state";
    private static final String DESCRIPTION = "State description";
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(6);
    }

    @Test
    void testNoArgsConstructor() {
        var state = getState();

        assertEquals(ID, state.getId());
        assertEquals(NAME, state.getName());
        assertEquals(DESCRIPTION, state.getDescription());

        assertEquals(NOW, state.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), state.getModifiedDateTime());
        assertNotNull(state.getReportJobs());

        var testState = new ReportJobState(-ID);
        assertEquals(-ID, testState.getId());
    }

    private static ReportJobState getState() {
        return new ReportJobState()
            .setId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setCreatedDateTime(NOW)
            .setModifiedDateTime(NOW.plusMinutes(2))
            .setReportJobs(Collections.singletonList(new ReportJob()));
    }
}