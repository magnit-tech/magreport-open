package ru.magnit.magreportbackend.mapper.filterreport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.dto.request.filterreport.FilterFieldAddRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class FilterReportFieldMergerTest {

    private static final long ID = 1L;
    private static final long REPORT_FIELD_ID = 2L;
    private static final long FILTER_INSTANCE_FIELD_ID = 3L;
    private static final String NAME = "Test field";
    private static final String DESCRIPTION = "Test field description";

    @InjectMocks
    private FilterReportFieldMapper mapper;

    @Test
    void from() {

        var result = mapper.from(getSource());

        assertNull(result.getId());
        assertEquals(NAME, result.getName());
        assertEquals(DESCRIPTION, result.getDescription());
        assertEquals(REPORT_FIELD_ID, result.getReportField().getId());
        assertEquals(FILTER_INSTANCE_FIELD_ID, result.getFilterInstanceField().getId());
    }

    private FilterFieldAddRequest getSource() {

        return new FilterFieldAddRequest()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setReportFieldId(REPORT_FIELD_ID)
                .setFilterInstanceFieldId(FILTER_INSTANCE_FIELD_ID);
    }
}