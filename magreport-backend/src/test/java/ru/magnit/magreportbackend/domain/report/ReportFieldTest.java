package ru.magnit.magreportbackend.domain.report;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.asm.ExternalAuth;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportField;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReportFieldTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "ReportField";
    private static final String DESCRIPTION = "ReportField description";
    private static final int ORDINAL = 0;
    private static final boolean VISIBLE = true;
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(11);
    }

    @Test
    void testNoArgsConstructor() {
        var testObject = getReportField();
        var testObject2 = new ExternalAuth(-ID);

        assertEquals(ID, testObject.getId());
        assertEquals(NAME, testObject.getName());
        assertEquals(DESCRIPTION, testObject.getDescription());
        assertEquals(ORDINAL, testObject.getOrdinal());
        assertEquals(VISIBLE, testObject.getVisible());

        assertNotNull(testObject.getDataSetField());
        assertNotNull(testObject.getPivotFieldType());
        assertNotNull(testObject.getReport());
        assertNotNull(testObject.getFilterReportFields());
        assertEquals(NOW, testObject.getCreatedDateTime());

        assertEquals(NOW.plusMinutes(2), testObject.getModifiedDateTime());

        assertEquals(-ID, testObject2.getId());
    }

    private static ReportField getReportField() {
        return new ReportField()
            .setId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setOrdinal(ORDINAL)
            .setVisible(VISIBLE)
            .setDataSetField(new DataSetField())
            .setPivotFieldType(new PivotFieldType())
            .setFilterReportFields(Collections.singletonList(new FilterReportField()))
            .setReport(new Report())
            .setCreatedDateTime(NOW)
            .setModifiedDateTime(NOW.plusMinutes(2));
    }
}