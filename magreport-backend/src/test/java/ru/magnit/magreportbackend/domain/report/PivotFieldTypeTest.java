package ru.magnit.magreportbackend.domain.report;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.asm.ExternalAuth;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PivotFieldTypeTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "PivotFieldType";
    private static final String DESCRIPTION = "PivotFieldType description";
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(6);
    }

    @Test
    void testNoArgsConstructor() {
        var testObject = getPivotFieldType();
        var testObject2 = new ExternalAuth(-ID);

        assertEquals(ID, testObject.getId());
        assertEquals(NAME, testObject.getName());
        assertEquals(DESCRIPTION, testObject.getDescription());

        assertEquals(NOW, testObject.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), testObject.getModifiedDateTime());
        assertNotNull(testObject.getFields());

        assertEquals(-ID, testObject2.getId());
    }

    private static PivotFieldType getPivotFieldType() {
        return new PivotFieldType()
            .setId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setFields(Collections.singletonList(new ReportField()))
            .setCreatedDateTime(NOW)
            .setModifiedDateTime(NOW.plusMinutes(2));
    }
}