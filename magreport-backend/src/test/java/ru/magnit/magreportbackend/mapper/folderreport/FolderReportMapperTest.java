package ru.magnit.magreportbackend.mapper.folderreport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.folderreport.FolderReport;
import ru.magnit.magreportbackend.dto.request.folderreport.FolderAddReportRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class FolderReportMapperTest {

    private final Long ID = 1L;

    @InjectMocks
    private FolderReportMapper mapper;

    @Test
    void from() {
        FolderReport folderReport = mapper.from(getRequest());

        assertNotNull(folderReport.getReport());
        assertEquals(ID, folderReport.getReport().getId());
        assertNotNull(folderReport.getFolder());
        assertEquals(ID, folderReport.getFolder().getId());

        List<FolderReport> folderReports = mapper.from(Collections.singletonList(getRequest()));
        assertNotEquals(0, folderReports.size());
        folderReport = folderReports.get(0);

        assertNotNull(folderReport.getReport());
        assertEquals(ID, folderReport.getReport().getId());
        assertNotNull(folderReport.getFolder());
        assertEquals(ID, folderReport.getFolder().getId());
    }

    private FolderAddReportRequest getRequest() {
        return new FolderAddReportRequest()
            .setFolderId(ID)
            .setReportId(ID);
    }
}