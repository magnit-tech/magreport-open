package ru.magnit.magreportbackend.domain.securityfilter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSecurityFilter;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationType;
import ru.magnit.magreportbackend.domain.user.User;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SecurityFilterTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "SecurityFilter";
    private static final String DESCRIPTION = "SecurityFilter description";
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(13);
    }

    @Test
    void testNoArgsConstructor() {
        var filter = getFilter();

        assertEquals(ID, filter.getId());
        assertEquals(NAME, filter.getName());
        assertEquals(DESCRIPTION, filter.getDescription());

        assertEquals(NOW, filter.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), filter.getModifiedDateTime());
        assertNotNull(filter.getAuthSecurityFilters());
        assertNotNull(filter.getFolder());
        assertNotNull(filter.getFieldMappings());
        assertNotNull(filter.getDataSets());
        assertNotNull(filter.getOperationType());
        assertNotNull(filter.getUser());

        var testFilter = new SecurityFilter(-ID);
        assertEquals(-ID, testFilter.getId());
    }

    private static SecurityFilter getFilter() {
        return new SecurityFilter()
                .setId(ID)
                .setOperationType(new FilterOperationType())
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW.plusMinutes(2))
                .setAuthSecurityFilters(Collections.singletonList(new ExternalAuthSecurityFilter()))
                .setFolder(new SecurityFilterFolder())
                .setFieldMappings(Collections.singletonList(new SecurityFilterDataSetField()))
                .setDataSets(Collections.singletonList(new SecurityFilterDataSet()))
                .setFilterRoles(Collections.singletonList(new SecurityFilterRole()))
                .setUser(new User());
    }
}