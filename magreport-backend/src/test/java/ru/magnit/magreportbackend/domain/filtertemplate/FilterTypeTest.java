package ru.magnit.magreportbackend.domain.filtertemplate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FilterTypeTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "Test filter type";
    private static final String DESCRIPTION = "type description";
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(6);
    }

    @Test
    void testNoArgsConstructor() {
        var filterType = getFilterType();

        assertEquals(ID, filterType.getId());
        assertEquals(NAME, filterType.getName());
        assertEquals(DESCRIPTION, filterType.getDescription());

        assertEquals(NOW, filterType.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), filterType.getModifiedDateTime());
        assertNotNull(filterType.getFilterTemplates());

        var testFilterType = new FilterType(-ID);
        assertEquals(-ID, testFilterType.getId());
    }

    private static FilterType getFilterType() {
        return new FilterType()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW.plusMinutes(2))
                .setFilterTemplates(Collections.singletonList(new FilterTemplate()));
    }
}