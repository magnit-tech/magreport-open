package ru.magnit.magreportbackend.domain.filterinstance;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportField;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateField;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterDataSetField;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FilterInstanceFieldTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "Test field";
    private static final String DESCRIPTION = "Field description";
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final long LEVEL = 1L;

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(12);
    }

    @Test
    void testNoArgsConstructor() {
        var field = getFilterField();

        assertEquals(ID, field.getId());
        assertEquals(NAME, field.getName());
        assertEquals(DESCRIPTION, field.getDescription());
        assertEquals(NOW, field.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), field.getModifiedDateTime());

        assertNotNull(field.getInstance());
        assertNotNull(field.getTemplateField());
        assertNotNull(field.getDataSetField());
        assertNotNull(field.getFilterReportFields());
        assertNotNull(field.getFieldMappings());

        var testField = new FilterInstanceField(-ID);
        assertEquals(-ID, testField.getId());
    }

    private static FilterInstanceField getFilterField() {
        return new FilterInstanceField()
                .setId(ID)
                .setLevel(LEVEL)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW.plusMinutes(2))
                .setInstance(new FilterInstance())
                .setTemplateField(new FilterTemplateField())
                .setDataSetField(new DataSetField())
                .setFilterReportFields(Collections.singletonList(new FilterReportField()))
                .setFieldMappings(Collections.singletonList(new SecurityFilterDataSetField()));
    }
}