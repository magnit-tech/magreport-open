package ru.magnit.magreportbackend.domain.filtertemplate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FilterFieldTypeTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "Test field type";
    private static final String DESCRIPTION = "Type description";
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(6);
    }

    @Test
    void testNoArgsConstructor() {
        var fieldType = getFilterFieldType();

        assertEquals(ID, fieldType.getId());
        assertEquals(NAME, fieldType.getName());
        assertEquals(DESCRIPTION, fieldType.getDescription());

        assertEquals(NOW, fieldType.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), fieldType.getModifiedDateTime());
        assertNotNull(fieldType.getFields());

        var testFieldType = new FilterFieldType(-ID);
        assertEquals(-ID, testFieldType.getId());
    }

    private static FilterFieldType getFilterFieldType() {
        return new FilterFieldType()
            .setId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setCreatedDateTime(NOW)
            .setModifiedDateTime(NOW.plusMinutes(2))
            .setFields(Collections.singletonList(new FilterTemplateField()));
    }
}