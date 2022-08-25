package ru.magnit.magreportbackend.domain.filtertemplate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstance;
import ru.magnit.magreportbackend.domain.user.User;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FilterTemplateTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "Test filter";
    private static final String DESCRIPTION = "Filter description";
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(11);
    }

    @Test
    void testNoArgsConstructor() {
        var filter = getFilter();

        assertEquals(ID, filter.getId());
        assertEquals(NAME, filter.getName());
        assertEquals(DESCRIPTION, filter.getDescription());
        assertEquals(NOW, filter.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), filter.getModifiedDateTime());

        assertNotNull(filter.getType());
        assertNotNull(filter.getFolder());
        assertNotNull(filter.getOperations());
        assertNotNull(filter.getFields());
        assertNotNull(filter.getFilterInstances());

        assertNotNull(filter.getUser());

        var testFilter = new FilterTemplate(-ID);
        assertEquals(-ID, testFilter.getId());
    }

    private static FilterTemplate getFilter() {
        return new FilterTemplate()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW.plusMinutes(2))
                .setType(new FilterType())
                .setFolder(new FilterTemplateFolder())
                .setOperations(Collections.singletonList(new FilterOperation()))
                .setFields(Collections.singletonList(new FilterTemplateField()))
                .setFilterInstances(Collections.singletonList(new FilterInstance()))
                .setUser(new User());
    }
}