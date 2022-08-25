package ru.magnit.magreportbackend.domain.report;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.asm.ExternalAuth;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.excel.ReportExcelTemplate;
import ru.magnit.magreportbackend.domain.excel.UserReportExcelTemplate;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportGroup;
import ru.magnit.magreportbackend.domain.folderreport.FolderReport;
import ru.magnit.magreportbackend.domain.olap.OlapConfiguration;
import ru.magnit.magreportbackend.domain.reportjob.ReportJob;
import ru.magnit.magreportbackend.domain.user.User;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReportTest extends BaseEntityTest {

    public static final long ID = 1L;
    public static final String NAME = "Report";
    public static final String DESCRIPTION = "Report description";
    public static final LocalDateTime CREATED_DATE_TIME = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(15);
    }

    @Test
    void testNoArgsConstructor() {
        var testObject = getReport();
        var testObject2 = new ExternalAuth(-ID);

        assertEquals(ID, testObject.getId());
        assertEquals(NAME, testObject.getName());
        assertEquals(DESCRIPTION, testObject.getDescription());
        assertEquals(CREATED_DATE_TIME, testObject.getCreatedDateTime());
        assertEquals(CREATED_DATE_TIME.plusMinutes(2), testObject.getModifiedDateTime());

        assertNotNull(testObject.getFields());
        assertNotNull(testObject.getFolder());
        assertNotNull(testObject.getReportExcelTemplates());
        assertNotNull(testObject.getUserReportExcelTemplates());
        assertNotNull(testObject.getFolderReports());

        assertNotNull(testObject.getDataSet());
        assertNotNull(testObject.getReportJobs());
        assertNotNull(testObject.getUser());
        assertNotNull(testObject.getFilterReportGroups());


        assertEquals(-ID, testObject2.getId());
    }

    private static Report getReport() {
        return new Report()
            .setId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setFields(Collections.singletonList(new ReportField()))
            .setFolder(new ReportFolder())

            .setReportExcelTemplates(Collections.singletonList(new ReportExcelTemplate()))
            .setUserReportExcelTemplates(Collections.singletonList(new UserReportExcelTemplate()))
            .setFolderReports(Collections.singletonList(new FolderReport()))
            .setReportJobs(Collections.singletonList(new ReportJob()))
            .setFilterReportGroups(Collections.singletonList(new FilterReportGroup()))
            .setDataSet(new DataSet())
            .setUser(new User())
            .setCreatedDateTime(CREATED_DATE_TIME)
            .setModifiedDateTime(CREATED_DATE_TIME.plusMinutes(2));
    }
}