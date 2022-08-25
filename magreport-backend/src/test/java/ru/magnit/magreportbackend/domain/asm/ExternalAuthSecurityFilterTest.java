package ru.magnit.magreportbackend.domain.asm;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilter;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExternalAuthSecurityFilterTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final LocalDateTime CREATED = LocalDateTime.now();
    private static final LocalDateTime MODIFIED = LocalDateTime.now().plusMinutes(2);

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(5);
    }

    @Test
    void testNoArgsConstructor() {
        var securityFilter = getExternalAuthSecurityFilter();
        var securityFilter2 = new ExternalAuthSecurityFilter(-ID);

        assertEquals(ID, securityFilter.getId());
        assertNotNull(securityFilter.getSource());
        assertNotNull(securityFilter.getSecurityFilter());
        assertEquals(CREATED, securityFilter.getCreatedDateTime());
        assertEquals(MODIFIED, securityFilter.getModifiedDateTime());

        assertEquals(-ID, securityFilter2.getId());
    }

    private static ExternalAuthSecurityFilter getExternalAuthSecurityFilter() {
        return new ExternalAuthSecurityFilter()
            .setId(ID)
            .setSource(new ExternalAuthSource())
            .setSecurityFilter(new SecurityFilter())
            .setCreatedDateTime(CREATED)
            .setModifiedDateTime(MODIFIED);
    }
}