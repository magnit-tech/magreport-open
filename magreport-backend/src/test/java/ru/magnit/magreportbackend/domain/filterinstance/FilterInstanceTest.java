package ru.magnit.magreportbackend.domain.filterinstance;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.filterreport.FilterReport;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplate;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilter;
import ru.magnit.magreportbackend.domain.user.User;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FilterInstanceTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "Test filter";
    private static final String CODE = "Filter code";
    private static final String DESCRIPTION = "Filter description";


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
        assertEquals(CODE, filter.getCode());
        assertEquals(DESCRIPTION, filter.getDescription());
        assertEquals(NOW, filter.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), filter.getModifiedDateTime());

        assertNotNull(filter.getFolder());
        assertNotNull(filter.getFilterTemplate());
        assertNotNull(filter.getFields());
        assertNotNull(filter.getDataSet());
        assertNotNull(filter.getFilterReports());

        assertNotNull(filter.getUser());
        assertNotNull(filter.getSecurityFilters());

        var testFilter = new FilterInstance(-ID);
        assertEquals(-ID, testFilter.getId());
    }

    private static FilterInstance getFilter() {
        return new FilterInstance()
                .setId(ID)
                .setName(NAME)
                .setCode(CODE)
                .setDescription(DESCRIPTION)
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW.plusMinutes(2))
                .setFolder(new FilterInstanceFolder())
                .setFilterTemplate(new FilterTemplate())
                .setDataSet(new DataSet())
                .setFields(Collections.singletonList(new FilterInstanceField()))
                .setFilterReports(Collections.singletonList(new FilterReport()))
                .setUser(new User())
                .setSecurityFilters(Collections.singletonList(new SecurityFilter()));

    }
}