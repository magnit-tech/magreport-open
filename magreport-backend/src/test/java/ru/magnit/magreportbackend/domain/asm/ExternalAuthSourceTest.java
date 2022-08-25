package ru.magnit.magreportbackend.domain.asm;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.dataset.DataSet;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExternalAuthSourceTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "ExternalAuthSource";
    private static final String DESCRIPTION = "ExternalAuthSource description";
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(12);
    }

    @Test
    void testNoArgsConstructor() {
        var externalAuth = getExternalAuthSource();
        var externalAuth2 = new ExternalAuthSource(-ID);

        assertEquals(ID, externalAuth.getId());
        assertEquals(NAME, externalAuth.getName());
        assertEquals(DESCRIPTION, externalAuth.getDescription());
        assertEquals(NOW, externalAuth.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), externalAuth.getModifiedDateTime());

        assertNotNull(externalAuth.getType());
        assertNotNull(externalAuth.getExternalAuth());
        assertNotNull(externalAuth.getDataSet());
        assertNotNull(externalAuth.getFields());
        assertNotNull(externalAuth.getSecurityFilters());

        assertEquals(-ID, externalAuth2.getId());
    }

    private static ExternalAuthSource getExternalAuthSource() {

        return new ExternalAuthSource()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setType(new ExternalAuthSourceType())
                .setExternalAuth(new ExternalAuth())

                .setDataSet(new DataSet())
                .setFields(Collections.singletonList(new ExternalAuthSourceField()))
                .setSecurityFilters(Collections.singletonList(new ExternalAuthSecurityFilter()))
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW.plusMinutes(2));
    }

}