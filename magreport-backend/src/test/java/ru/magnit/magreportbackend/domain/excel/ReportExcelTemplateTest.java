package ru.magnit.magreportbackend.domain.excel;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.report.Report;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReportExcelTemplateTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(7);
    }

    @Test
    void testNoArgsConstructor() {
        var reportExcelTemplate = getReportExcelTemplate();

        assertEquals(ID, reportExcelTemplate.getId());
        assertEquals(NOW, reportExcelTemplate.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), reportExcelTemplate.getModifiedDateTime());
        assertNotNull(reportExcelTemplate.getExcelTemplate());
        assertNotNull(reportExcelTemplate.getReport());

        assertNotNull(reportExcelTemplate.getUserReportExcelTemplates());
        assertTrue(reportExcelTemplate.getIsDefault());

        var reportExcelTemplate1 = new ReportExcelTemplate(-ID);
        assertEquals(-ID, reportExcelTemplate1.getId());
    }

    private static ReportExcelTemplate getReportExcelTemplate() {

        return new ReportExcelTemplate()
                .setId(ID)
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW.plusMinutes(2))
                .setExcelTemplate(new ExcelTemplate())
                .setReport(new Report())

                .setUserReportExcelTemplates(Collections.singletonList(new UserReportExcelTemplate()))
                .setIsDefault(true);
    }
}