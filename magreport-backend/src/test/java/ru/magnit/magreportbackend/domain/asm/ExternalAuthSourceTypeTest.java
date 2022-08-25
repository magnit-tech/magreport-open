package ru.magnit.magreportbackend.domain.asm;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExternalAuthSourceTypeTest extends BaseEntityTest {

    public static final long ID = 1L;
    public static final String NAME = "getExternalAuthSourceType";
    public static final String DESCRIPTION = "getExternalAuthSourceType description";
    public static final LocalDateTime CREATED_DATE = LocalDateTime.now().minusDays(5L);
    public static final LocalDateTime MODIFIED_DATE = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(6);
    }

    @Test
    void testNoArgsConstructor() {
        var externalAuth = getExternalAuthSourceType();
        var externalAuth2 = new ExternalAuth(-ID);

        assertEquals(ID, externalAuth.getId());
        assertEquals(NAME, externalAuth.getName());
        assertEquals(DESCRIPTION, externalAuth.getDescription());
        assertNotNull(externalAuth.getSources());

        assertEquals(-ID, externalAuth2.getId());
    }

    private static ExternalAuthSourceType getExternalAuthSourceType() {
        return new ExternalAuthSourceType()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setSources(Collections.singletonList(new ExternalAuthSource()))
                .setCreatedDateTime(CREATED_DATE)
                .setModifiedDateTime(MODIFIED_DATE);
    }

}