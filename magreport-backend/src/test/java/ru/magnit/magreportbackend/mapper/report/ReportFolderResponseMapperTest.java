package ru.magnit.magreportbackend.mapper.report;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.report.ReportFolder;
import ru.magnit.magreportbackend.dto.response.report.ReportFolderResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportFolderResponseMapperTest {

    private static final long PARENT_ID = 2L;
    private static final long ID = 1L;
    private static final String NAME = "Test";
    private static final String DESCRIPTION = "description";
    private static final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    @InjectMocks
    private ReportFolderResponseMapper mapper;

    @Mock
    private ReportResponseMapper reportResponseMapper;

    @Test
    void from() {
        when(reportResponseMapper.from(Collections.singletonList(any()))).thenReturn(Collections.singletonList(new ReportResponse()));

        ReportFolderResponse response = mapper.from(getReportFolder());

        assertEquals(ID, response.getId());
        assertEquals(PARENT_ID, response.getParentId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getReports());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        List<ReportFolderResponse> responses = mapper.from(Collections.singletonList(getReportFolder()));
        assertNotEquals(0, responses.size());
        response = responses.get(0);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getReports());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());
    }

    private ReportFolder getReportFolder() {

        return new ReportFolder()
                .setId(ID)
                .setParentFolder(new ReportFolder(PARENT_ID))
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreatedDateTime(CREATED_TIME)
                .setModifiedDateTime(MODIFIED_TIME);
    }
}