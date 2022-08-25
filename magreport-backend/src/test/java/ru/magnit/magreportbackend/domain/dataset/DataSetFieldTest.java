package ru.magnit.magreportbackend.domain.dataset;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceField;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;
import ru.magnit.magreportbackend.domain.report.ReportField;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterDataSetField;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DataSetFieldTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "Test field";
    private static final String DESCRIPTION = "Field description";
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final boolean IS_SYNC = true;

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(12);
    }

    @Test
    void testNoArgsConstructor() {
        var field = getField();

        assertEquals(ID, field.getId());
        assertEquals(NAME, field.getName());
        assertEquals(DESCRIPTION, field.getDescription());
        assertEquals(NOW, field.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), field.getModifiedDateTime());

        assertEquals(IS_SYNC, field.getIsSync());
        assertNotNull(field.getType());
        assertNotNull(field.getDataSet());
        assertNotNull(field.getAuthSourceFields());
        assertNotNull(field.getInstanceFields());

        assertNotNull(field.getFieldMappings());
        assertNotNull(field.getReportFields());

        var testField = new DataSetField(-ID);
        assertEquals(-ID, testField.getId());
    }

    private static DataSetField getField() {
        return new DataSetField()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW.plusMinutes(2))
                .setIsSync(IS_SYNC)
                .setType(new DataType())
                .setDataSet(new DataSet())
                .setAuthSourceFields(Collections.singletonList(new ExternalAuthSourceField()))
                .setInstanceFields(Collections.singletonList(new FilterInstanceField()))
                .setFieldMappings(Collections.singletonList(new SecurityFilterDataSetField()))
                .setReportFields(Collections.singletonList(new ReportField()));
    }
}