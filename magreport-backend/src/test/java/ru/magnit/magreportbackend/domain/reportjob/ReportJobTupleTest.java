package ru.magnit.magreportbackend.domain.reportjob;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReportJobTupleTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final LocalDateTime CREATED_DATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_DATE_TIME = CREATED_DATE_TIME.plusMinutes(2);

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(5);
    }

    @Test
    void testNoArgsConstructor() {
        var objectUnderTest = getReportJobTuple();

        assertEquals(ID, objectUnderTest.getId());
        assertEquals(CREATED_DATE_TIME, objectUnderTest.getCreatedDateTime());
        assertEquals(MODIFIED_DATE_TIME, objectUnderTest.getModifiedDateTime());

        assertNotNull(objectUnderTest.getReportJobFilter());
        assertNotNull(objectUnderTest.getFields());

        var testObject = new ReportJob(-ID);
        assertEquals(-ID, testObject.getId());
    }

    private ReportJobTuple getReportJobTuple() {

        return new ReportJobTuple()
                .setId(ID)
                .setReportJobFilter(new ReportJobFilter())
                .setFields(Collections.singletonList(new ReportJobTupleField()))
                .setCreatedDateTime(CREATED_DATE_TIME)
                .setModifiedDateTime(MODIFIED_DATE_TIME);
    }
}