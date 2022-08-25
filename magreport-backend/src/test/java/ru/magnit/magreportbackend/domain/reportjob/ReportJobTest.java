package ru.magnit.magreportbackend.domain.reportjob;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.user.User;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReportJobTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String TEST_MESSAGE = "Test message";
    private static final LocalDateTime CREATED_DATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_DATE_TIME = CREATED_DATE_TIME.plusMinutes(2);
    private static final long ROW_COUNT = 10L;
    private static final String SQL_QUERY = "SQL QUERY";

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(13);
    }

    @Test
    void testNoArgsConstructor() {
        var objectUnderTest = getReportJob();

        assertEquals(ID, objectUnderTest.getId());
        assertEquals(TEST_MESSAGE, objectUnderTest.getMessage());
        assertEquals(SQL_QUERY, objectUnderTest.getSqlQuery());
        assertEquals(CREATED_DATE_TIME, objectUnderTest.getCreatedDateTime());
        assertEquals(MODIFIED_DATE_TIME, objectUnderTest.getModifiedDateTime());

        assertEquals(ROW_COUNT, objectUnderTest.getRowCount());
        assertNotNull(objectUnderTest.getReport());
        assertNotNull(objectUnderTest.getUser());
        assertNotNull(objectUnderTest.getStatus());
        assertNotNull(objectUnderTest.getState());

        assertNotNull(objectUnderTest.getReportJobFilters());

        var testObject = new ReportJob(-ID);
        assertEquals(-ID, testObject.getId());
    }

    private ReportJob getReportJob() {

        return new ReportJob()
                .setId(ID)
                .setMessage(TEST_MESSAGE)
                .setSqlQuery(SQL_QUERY)
                .setUser(new User())
                .setState(new ReportJobState())

                .setStatus(new ReportJobStatus())
                .setRowCount(ROW_COUNT)
                .setReport(new Report())
                .setReportJobFilters(Collections.singletonList(new ReportJobFilter()))
                .setCreatedDateTime(CREATED_DATE_TIME)

                .setModifiedDateTime(MODIFIED_DATE_TIME);
    }
}