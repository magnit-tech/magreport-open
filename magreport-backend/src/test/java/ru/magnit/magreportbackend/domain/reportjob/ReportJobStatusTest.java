package ru.magnit.magreportbackend.domain.reportjob;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReportJobStatusTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "Test status";
    private static final String DESCRIPTION = "Status description";
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(6);
    }

    @Test
    void testNoArgsConstructor() {
        var status = getStatus();

        assertEquals(ID, status.getId());
        assertEquals(NAME, status.getName());
        assertEquals(DESCRIPTION, status.getDescription());

        assertEquals(NOW, status.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), status.getModifiedDateTime());
        assertNotNull(status.getReportJobs());

        var testStatus = new ReportJobStatus(-ID);
        assertEquals(-ID, testStatus.getId());
    }

    private static ReportJobStatus getStatus() {

        return new ReportJobStatus()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW.plusMinutes(2))
                .setReportJobs(Collections.singletonList(new ReportJob()));
    }
}
