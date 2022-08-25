package ru.magnit.magreportbackend.domain.asm;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExternalAuthSourceFieldTypeTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "ExternalAuthSourceFieldType";
    private static final String DESCRIPTION = "ExternalAuthSourceFieldType description";
    private static final LocalDateTime CREATED = LocalDateTime.now();
    private static final LocalDateTime MODIFIED = LocalDateTime.now().plusMinutes(2);

    @BeforeAll
    void init() throws ClassNotFoundException {
        checkNumberOfFields(6);
    }

    @Test
    void testNoArgsConstructor() {
        var externalAuth = getExternalAuthSourceFieldType();
        var externalAuth2 = new ExternalAuth(-ID);

        assertEquals(ID, externalAuth.getId());
        assertEquals(NAME, externalAuth.getName());
        assertEquals(DESCRIPTION, externalAuth.getDescription());

        assertNotNull(externalAuth.getFields());
        assertEquals(CREATED, externalAuth.getCreatedDateTime());
        assertEquals(MODIFIED, externalAuth.getModifiedDateTime());

        assertEquals(-ID, externalAuth2.getId());
    }

    private static ExternalAuthSourceFieldType getExternalAuthSourceFieldType() {
        return new ExternalAuthSourceFieldType()
            .setId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setFields(Collections.singletonList(new ExternalAuthSourceField()))
            .setCreatedDateTime(CREATED)
            .setModifiedDateTime(MODIFIED);
    }

}