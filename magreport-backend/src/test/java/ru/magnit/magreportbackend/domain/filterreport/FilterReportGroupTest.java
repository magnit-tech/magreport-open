package ru.magnit.magreportbackend.domain.filterreport;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.report.Report;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FilterReportGroupTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "Test group";
    private static final String DESCRIPTION = "Group description";
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final boolean LINKED_FILTERS = true;
    private static final long ORDINAL = 1L;
    private static final String CODE = "CODE";
    private static final boolean MANDATORY = true;

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(14);
    }

    @Test
    void testNoArgsConstructor() {
        var group = getGroup();

        assertEquals(ID, group.getId());
        assertEquals(NAME, group.getName());
        assertEquals(DESCRIPTION, group.getDescription());
        assertEquals(ORDINAL, group.getOrdinal());
        assertEquals(NOW, group.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), group.getModifiedDateTime());

        assertNotNull(group.getType());
        assertNotNull(group.getReport());
        assertNotNull(group.getParentGroup());
        assertNotNull(group.getFilterReports());
        assertNotNull(group.getChildGroups());

        assertEquals(LINKED_FILTERS, group.getLinkedFilters());
        assertEquals(MANDATORY, group.getMandatory());
        assertEquals(CODE, group.getCode());

        var testGroup = new FilterReportGroup(-ID);
        assertEquals(-ID, testGroup.getId());
    }

    private static FilterReportGroup getGroup() {
        return new FilterReportGroup()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setOrdinal(ORDINAL)
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW.plusMinutes(2))
                .setType(new GroupOperationType())
                .setReport(new Report())
                .setParentGroup(new FilterReportGroup())
                .setChildGroups(Collections.singletonList(new FilterReportGroup()))
                .setFilterReports(Collections.singletonList(new FilterReport()))
                .setLinkedFilters(LINKED_FILTERS)
                .setMandatory(MANDATORY)
                .setCode(CODE);
    }
}