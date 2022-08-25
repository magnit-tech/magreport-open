package ru.magnit.magreportbackend.domain.filterreport;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;
import ru.magnit.magreportbackend.domain.report.ReportField;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobTupleField;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FilterReportFieldTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "Test filter";
    private static final String DESCRIPTION = "Filter description";
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final long ORDINAL = 1L;

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(11);
    }

    @Test
    void testNoArgsConstructor() {
        var filter = getFilter();

        assertEquals(ID, filter.getId());
        assertEquals(NAME, filter.getName());
        assertEquals(DESCRIPTION, filter.getDescription());
        assertEquals(ORDINAL, filter.getOrdinal());
        assertEquals(NOW, filter.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), filter.getModifiedDateTime());

        assertNotNull(filter.getReportField());
        assertNotNull(filter.getFilterInstanceField());
        assertNotNull(filter.getFilterReport());
        assertNotNull(filter.getReportJobTupleFields());

        var testFilter = new FilterReportField(-ID);
        assertEquals(-ID, testFilter.getId());
    }

    private static FilterReportField getFilter() {
        return new FilterReportField()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setOrdinal(ORDINAL)
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW.plusMinutes(2))
                .setReportField(new ReportField())
                .setFilterReport(new FilterReport())
                .setFilterInstanceField(new FilterInstanceField())
                .setReportJobTupleFields(Collections.singletonList(new ReportJobTupleField()));
    }
}