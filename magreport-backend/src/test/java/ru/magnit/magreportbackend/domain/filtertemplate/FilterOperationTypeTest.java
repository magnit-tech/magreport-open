package ru.magnit.magreportbackend.domain.filtertemplate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobFilter;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilter;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FilterOperationTypeTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "Test type";
    private static final String DESCRIPTION = "type description";
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(8);
    }

    @Test
    void testNoArgsConstructor() {
        var type = getType();

        assertEquals(ID, type.getId());
        assertEquals(NAME, type.getName());
        assertEquals(DESCRIPTION, type.getDescription());
        assertEquals(NOW, type.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), type.getModifiedDateTime());

        assertNotNull(type.getOperations());
        assertNotNull(type.getReportJobFilters());
        assertNotNull(type.getSecurityFilters());

        var testType = new FilterOperationType(-ID);
        assertEquals(-ID, testType.getId());
    }

    private static FilterOperationType getType() {
        return new FilterOperationType()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW.plusMinutes(2))
                .setOperations(Collections.singletonList(new FilterOperation()))
                .setReportJobFilters(Collections.singletonList(new ReportJobFilter()))
                .setSecurityFilters(Collections.singletonList(new SecurityFilter()));
    }
}