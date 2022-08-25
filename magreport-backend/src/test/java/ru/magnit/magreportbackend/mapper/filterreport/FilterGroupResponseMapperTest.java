package ru.magnit.magreportbackend.mapper.filterreport;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import ru.magnit.magreportbackend.domain.filterreport.FilterReport;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportGroup;
import ru.magnit.magreportbackend.domain.filterreport.GroupOperationType;
import ru.magnit.magreportbackend.domain.filterreport.GroupOperationTypeEnum;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FilterGroupResponseMapperTest {

    private static final long ID = 1L;
    private static final String NAME = "Test report group";
    private static final String DESCRIPTION = "Test report group description";
    private static final long REPORT_ID = 2L;
    private static final LocalDateTime CREATED_DATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_DATE_TIME = CREATED_DATE_TIME.plusMinutes(2);

    @InjectMocks
    private FilterGroupResponseMapper mapper;

    @Mock
    private FilterReportResponseMapper reportMapper;

    @Test
    void from() {
        given(reportMapper.from(ArgumentMatchers.<List<FilterReport>>any())).willReturn(Collections.singletonList(getFilterReport()));

        var response = mapper.from(getSource());

        assertEquals(ID, response.id());
        assertEquals(NAME, response.name());
        assertEquals(DESCRIPTION, response.description());
        assertEquals(GroupOperationTypeEnum.AND, response.operationType());
        assertEquals(CREATED_DATE_TIME, response.created());
        assertEquals(MODIFIED_DATE_TIME, response.modified());
        assertEquals(false, response.linkedFilters());
        assertEquals(REPORT_ID, response.reportId());
        assertNotNull(response.childGroups());
        assertNotNull(response.filters());
    }

    private FilterReportResponse getFilterReport() {

        return new FilterReportResponse();
    }

    private FilterReportGroup getSource() {

        return new FilterReportGroup()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setType(new GroupOperationType(GroupOperationTypeEnum.AND))
                .setLinkedFilters(false)
                .setReport(new Report(REPORT_ID))
                .setChildGroups(Collections.emptyList())
                .setFilterReports(Collections.emptyList())
                .setCreatedDateTime(CREATED_DATE_TIME)
                .setModifiedDateTime(MODIFIED_DATE_TIME);
    }
}