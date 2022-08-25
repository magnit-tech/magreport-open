package ru.magnit.magreportbackend.domain.excel;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.user.User;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExcelTemplateTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "Test template";
    private static final String DESCRIPTION = "Template description";
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(8);
    }

    @Test
    void testNoArgsConstructor() {
        var excelTemplate = getExcelTemplate();

        assertEquals(ID, excelTemplate.getId());
        assertEquals(NAME, excelTemplate.getName());
        assertEquals(DESCRIPTION, excelTemplate.getDescription());

        assertEquals(NOW, excelTemplate.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), excelTemplate.getModifiedDateTime());
        assertNotNull(excelTemplate.getFolder());
        assertNotNull(excelTemplate.getReportExcelTemplates());
        assertNotNull(excelTemplate.getUser());

        var testTemplate = new ExcelTemplate(-ID);
        assertEquals(-ID, testTemplate.getId());
    }

    private static ExcelTemplate getExcelTemplate() {
        return new ExcelTemplate()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW.plusMinutes(2))
                .setFolder(new ExcelTemplateFolder())
                .setReportExcelTemplates(Collections.singletonList(new ReportExcelTemplate()))
                .setUser(new User());
    }
}