package ru.magnit.magreportbackend.mapper.report;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.report.ReportFolder;
import ru.magnit.magreportbackend.dto.response.report.ReportResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class ReportFolderResponse2MapperTest {

    private static final long REPORT_ID = 12L;
    private static final String REPORT_NAME = "Test report";
    private static final long PARENT_FOLDER_ID = 2L;
    private static final long FOLDER_ID = 1L;
    private static final String FOLDER_NAME = "Test folder";
    private static final String FOLDER_DESCRIPTION = "Test folder description";
    private static final LocalDateTime CREATED_DATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_DATE_TIME = CREATED_DATE_TIME.plusMinutes(2);

    @InjectMocks
    private ReportFolderResponse2Mapper mapper;

    @Mock
    private ReportResponseMapper reportMapper;

    @Test
    void from() {

        given(reportMapper.from(ArgumentMatchers.<List<Report>>any())).willReturn(Collections.singletonList(getReportResponse()));

        var result = mapper.from(getSource());

        assertEquals(FOLDER_ID, result.getId());
        assertEquals(FOLDER_NAME, result.getName());
        assertEquals(FOLDER_DESCRIPTION, result.getDescription());
        assertEquals(PARENT_FOLDER_ID, result.getParentId());
        assertEquals(REPORT_ID, result.getReports().get(0).getId());
        assertEquals(REPORT_NAME, result.getReports().get(0).getName());
        assertEquals(CREATED_DATE_TIME, result.getCreated());
        assertEquals(MODIFIED_DATE_TIME, result.getModified());

        assertNotNull(result.getReports());
    }

    private ReportFolder getSource() {
        return new ReportFolder()
                .setId(FOLDER_ID)
                .setName(FOLDER_NAME)
                .setDescription(FOLDER_DESCRIPTION)
                .setParentFolder(new ReportFolder(PARENT_FOLDER_ID))
                .setReports(Collections.singletonList(new Report(REPORT_ID)))
                .setCreatedDateTime(CREATED_DATE_TIME)
                .setModifiedDateTime(MODIFIED_DATE_TIME);
    }

    private ReportResponse getReportResponse() {
        return new ReportResponse()
                .setId(REPORT_ID)
                .setName(REPORT_NAME);
    }

    @Test
    void shallowMap() {
        var result = mapper.shallowMap(getSource());

        assertEquals(FOLDER_ID, result.getId());
        assertEquals(FOLDER_NAME, result.getName());
        assertEquals(FOLDER_DESCRIPTION, result.getDescription());
        assertEquals(PARENT_FOLDER_ID, result.getParentId());
        assertEquals(CREATED_DATE_TIME, result.getCreated());
        assertEquals(MODIFIED_DATE_TIME, result.getModified());

        assertNotNull(result.getReports());
    }
}