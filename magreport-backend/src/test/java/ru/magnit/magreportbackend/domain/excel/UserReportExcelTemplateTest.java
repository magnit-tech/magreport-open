package ru.magnit.magreportbackend.domain.excel;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserReportExcelTemplateTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(6);
    }

    @Test
    void testNoArgsConstructor() {
        var userReportExcelTemplate = getUserReportExcelTemplate();

        assertEquals(ID, userReportExcelTemplate.getId());
        assertEquals(NOW, userReportExcelTemplate.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), userReportExcelTemplate.getModifiedDateTime());
        assertNotNull(userReportExcelTemplate.getReportExcelTemplate());
        assertNotNull(userReportExcelTemplate.getReport());
        assertNotNull(userReportExcelTemplate.getUser());

        var userReportExcelTemplate1 = new UserReportExcelTemplate(-ID);
        assertEquals(-ID, userReportExcelTemplate1.getId());
    }

    private static UserReportExcelTemplate getUserReportExcelTemplate() {
        return new UserReportExcelTemplate()
            .setId(ID)
            .setCreatedDateTime(NOW)
            .setModifiedDateTime(NOW.plusMinutes(2))
            .setReportExcelTemplate(new ReportExcelTemplate())
            .setReport(new Report())
            .setUser(new User());
    }
}