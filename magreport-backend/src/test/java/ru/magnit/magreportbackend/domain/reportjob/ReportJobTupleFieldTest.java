package ru.magnit.magreportbackend.domain.reportjob;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportField;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReportJobTupleFieldTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(6);
    }

    @Test
    void testNoArgsConstructor() {
        var field = getField();

        assertEquals(ID, field.getId());
        assertEquals(NOW, field.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), field.getModifiedDateTime());

        assertNotNull(field.getFilterReportField());
        assertNotNull(field.getReportJobTuple());

        var testField = new ReportJobTupleField(-ID);
        assertEquals(-ID, testField.getId());
    }

    private static ReportJobTupleField getField() {

        return new ReportJobTupleField()
                .setId(ID)
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW.plusMinutes(2))
                .setFilterReportField(new FilterReportField())
                .setReportJobTuple(new ReportJobTuple());
    }
}