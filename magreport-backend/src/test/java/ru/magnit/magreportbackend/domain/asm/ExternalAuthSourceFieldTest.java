package ru.magnit.magreportbackend.domain.asm;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExternalAuthSourceFieldTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final LocalDateTime CREATED = LocalDateTime.now();
    private static final LocalDateTime MODIFIED = LocalDateTime.now().plusMinutes(2);

    @BeforeAll
    void init() throws ClassNotFoundException {
        checkNumberOfFields(7);
    }

    @Test
    void testNoArgsConstructor() {
        var externalAuth = getExternalAuthSourceField();
        var externalAuth2 = new ExternalAuth(-ID);

        assertEquals(ID, externalAuth.getId());
        assertNotNull(externalAuth.getType());
        assertNotNull(externalAuth.getSource());

        assertNotNull(externalAuth.getDataSetField());
        assertNotNull(externalAuth.getFilterInstanceFields());
        assertEquals(CREATED, externalAuth.getCreatedDateTime());
        assertEquals(MODIFIED, externalAuth.getModifiedDateTime());

        assertEquals(-ID, externalAuth2.getId());
    }

    private static ExternalAuthSourceField getExternalAuthSourceField() {
        return new ExternalAuthSourceField()
                .setId(ID)
                .setType(new ExternalAuthSourceFieldType())
                .setSource(new ExternalAuthSource())
                .setDataSetField(new DataSetField())
                .setFilterInstanceFields(Collections.singletonList(new ExternalAuthSourceFieldFilterInstanceField()))
                .setCreatedDateTime(CREATED)
                .setModifiedDateTime(MODIFIED);
    }

}