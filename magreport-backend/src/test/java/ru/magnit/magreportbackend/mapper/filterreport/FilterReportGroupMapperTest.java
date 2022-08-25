package ru.magnit.magreportbackend.mapper.filterreport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.filterreport.FilterReport;
import ru.magnit.magreportbackend.domain.filterreport.GroupOperationTypeEnum;
import ru.magnit.magreportbackend.dto.request.filterreport.FilterAddRequest;
import ru.magnit.magreportbackend.dto.request.filterreport.FilterGroupAddRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FilterReportGroupMapperTest {

    private static final long ID = 1L;
    private static final String NAME = "Test filter group";
    private static final String DESCRIPTION = "Test filter group description";
    private static final long REPORT_ID = 2L;

    @InjectMocks
    private FilterReportGroupMapper mapper;

    @Mock
    private FilterReportMapper reportMapper;

    @Test
    void from() {
        given(reportMapper.from(ArgumentMatchers.<List<FilterAddRequest>>any())).willReturn(Collections.singletonList(getFilterReport()));

        var result = mapper.from(getSource());

        assertNull(result.getId());
        assertEquals(NAME, result.getName());
        assertEquals(DESCRIPTION, result.getDescription());
        assertEquals(REPORT_ID, result.getReport().getId());
        assertEquals(GroupOperationTypeEnum.AND, result.getTypeEnum());
        assertEquals(false, result.getLinkedFilters());
        assertNotNull(result.getChildGroups());
        assertNotNull(result.getFilterReports());

    }

    private FilterReport getFilterReport() {

        return new FilterReport();
    }

    private FilterGroupAddRequest getSource() {

        return new FilterGroupAddRequest()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setReportId(REPORT_ID)
                .setOperationType(GroupOperationTypeEnum.AND)
                .setLinkedFilters(false)
                .setChildGroups(Collections.emptyList())
                .setFilters(Collections.emptyList());
    }
}