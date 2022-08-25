package ru.magnit.magreportbackend.mapper.report;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.dto.request.report.ReportAddRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ReportMapperTest {

    private final long ID = 1L;
    private final String NAME = "Test";
    private final String DESCRIPTION = "description";

    @InjectMocks
    private ReportMapper mapper;

    @Test
    void from() {
        Report report = mapper.from(getRequest());

        assertEquals(NAME, report.getName());
        assertEquals(DESCRIPTION, report.getDescription());
        assertNotNull(report.getFolder());
        assertNotNull(report.getDataSet());

        List<Report> reports = mapper.from(Collections.singletonList(getRequest()));
        assertNotEquals(0, reports.size());
        report = reports.get(0);

        assertEquals(NAME, report.getName());
        assertEquals(DESCRIPTION, report.getDescription());
        assertNotNull(report.getFolder());
        assertNotNull(report.getDataSet());
    }

    private ReportAddRequest getRequest() {
        return new ReportAddRequest()
            .setFolderId(ID)
            .setDataSetId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION);
    }
}