package ru.magnit.magreportbackend.domain.filtertemplate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FilterTemplateFieldTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "Test field";
    private static final String DESCRIPTION = "Field description";
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(8);
    }

    @Test
    void testNoArgsConstructor() {
        var field = getFilterField();

        assertEquals(ID, field.getId());
        assertEquals(NAME, field.getName());
        assertEquals(DESCRIPTION, field.getDescription());
        assertEquals(NOW, field.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), field.getModifiedDateTime());

        assertNotNull(field.getFilterTemplate());
        assertNotNull(field.getType());

        assertNotNull(field.getInstanceFields());

        var testField = new FilterTemplateField(-ID);
        assertEquals(-ID, testField.getId());
    }

    private static FilterTemplateField getFilterField() {
        return new FilterTemplateField()
            .setId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setCreatedDateTime(NOW)
            .setModifiedDateTime(NOW.plusMinutes(2))
            .setFilterTemplate(new FilterTemplate())
            .setType(new FilterFieldType())
            .setInstanceFields(Collections.singletonList(new FilterInstanceField()));
    }
}