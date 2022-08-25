package ru.magnit.magreportbackend.mapper.report;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.response.report.ReportResponse;
import ru.magnit.magreportbackend.mapper.auth.UserResponseMapper;
import ru.magnit.magreportbackend.mapper.filterreport.FilterGroupResponseMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
class ReportResponseMapperTest {

    private final long ID = 1L;
    private final String NAME = "Test";
    private final String DESCRIPTION = "description";
    private final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    @InjectMocks
    private ReportResponseMapper mapper;

    @Mock
    private ReportFieldResponseMapper reportFieldResponseMapper;

    @Mock
    private FilterGroupResponseMapper filterGroupResponseMapper;

    @Mock
    private UserResponseMapper userResponseMapper;

    @Test
    void from() {
        ReportResponse response = mapper.from(getReport());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(ID, response.getDataSetId());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        List<ReportResponse> responses = mapper.from(Collections.singletonList(getReport()));
        assertNotEquals(0, responses.size());
        response = responses.get(0);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(ID, response.getDataSetId());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());
    }

    private Report getReport() {
        Report report = new Report();
        report.setId(ID);
        report.setName(NAME);
        report.setDescription(DESCRIPTION);
        report.setDataSet(new DataSet().setId(ID));
        report.setCreatedDateTime(CREATED_TIME);
        report.setModifiedDateTime(MODIFIED_TIME);
        report.setUser(new User());
        return report;
    }
}