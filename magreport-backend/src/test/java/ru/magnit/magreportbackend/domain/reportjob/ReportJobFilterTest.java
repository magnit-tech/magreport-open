package ru.magnit.magreportbackend.domain.reportjob;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.filterreport.FilterReport;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationType;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReportJobFilterTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final LocalDateTime CREATED_DATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_DATE_TIME = CREATED_DATE_TIME.plusMinutes(2);

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(8);
    }

    @Test
    void testNoArgsConstructor() {
        var objectUnderTest = getReportJobFilter();

        assertEquals(ID, objectUnderTest.getId());
        assertEquals(CREATED_DATE_TIME, objectUnderTest.getCreatedDateTime());
        assertEquals(MODIFIED_DATE_TIME, objectUnderTest.getModifiedDateTime());

        assertNotNull(objectUnderTest.getReportJob());
        assertNotNull(objectUnderTest.getFilterReport());
        assertNotNull(objectUnderTest.getFilterOperationType());
        assertNotNull(objectUnderTest.getTuples());

        var testObject = new ReportJobFilter(-ID);
        assertEquals(-ID, testObject.getId());
    }

    private ReportJobFilter getReportJobFilter() {

        return new ReportJobFilter()
                .setId(ID)
                .setReportJob(new ReportJob())
                .setFilterReport(new FilterReport())
                .setFilterOperationType(new FilterOperationType())
                .setTuples(Collections.singletonList(new ReportJobTuple()))

                .setCreatedDateTime(CREATED_DATE_TIME)
                .setModifiedDateTime(MODIFIED_DATE_TIME);
    }
}