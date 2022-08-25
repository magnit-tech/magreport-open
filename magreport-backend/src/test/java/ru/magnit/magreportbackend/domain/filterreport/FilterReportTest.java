package ru.magnit.magreportbackend.domain.filterreport;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstance;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobFilter;
import ru.magnit.magreportbackend.domain.user.User;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FilterReportTest extends BaseEntityTest {

    private static final boolean ROOT_SELECTABLE = true;
    private static final long ID = 1L;
    private static final LocalDateTime CREATED_DATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_DATE_TIME = CREATED_DATE_TIME.plusMinutes(2);
    private static final boolean HIDDEN = true;
    private static final boolean MANDATORY = true;
    private static final long ORDINAL = 2L;
    private static final String NAME = "Test name";
    private static final String DESCRIPTION = "Test description";
    private static final String CODE = "CODE";

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(15);
    }

    @Test
    void testNoArgsConstructor() {
        var objectUnderTest = getFilterReport();

        assertEquals(ID, objectUnderTest.getId());
        assertEquals(NAME, objectUnderTest.getName());
        assertEquals(DESCRIPTION, objectUnderTest.getDescription());
        assertEquals(ROOT_SELECTABLE, objectUnderTest.isRootSelectable());
        assertEquals(HIDDEN, objectUnderTest.isHidden());

        assertEquals(MANDATORY, objectUnderTest.isMandatory());
        assertEquals(ORDINAL, objectUnderTest.getOrdinal());
        assertNotNull(objectUnderTest.getGroup());
        assertNotNull(objectUnderTest.getFilterInstance());
        assertNotNull(objectUnderTest.getFields());

        assertNotNull(objectUnderTest.getReportJobFilters());
        assertNotNull(objectUnderTest.getUser());
        assertEquals(CREATED_DATE_TIME, objectUnderTest.getCreatedDateTime());
        assertEquals(MODIFIED_DATE_TIME, objectUnderTest.getModifiedDateTime());
        assertEquals(CODE, objectUnderTest.getCode());

        var testObject = new FilterReport(-ID);
        assertEquals(-ID, testObject.getId());
    }

    private FilterReport getFilterReport() {

        return new FilterReport()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setRootSelectable(ROOT_SELECTABLE)
                .setHidden(HIDDEN)
                .setMandatory(MANDATORY)
                .setOrdinal(ORDINAL)
                .setGroup(new FilterReportGroup())
                .setFilterInstance(new FilterInstance())
                .setFields(Collections.singletonList(new FilterReportField()))

                .setReportJobFilters(Collections.singletonList(new ReportJobFilter()))
                .setUser(new User())
                .setCreatedDateTime(CREATED_DATE_TIME)
                .setModifiedDateTime(MODIFIED_DATE_TIME)
                .setCode(CODE);
    }
}