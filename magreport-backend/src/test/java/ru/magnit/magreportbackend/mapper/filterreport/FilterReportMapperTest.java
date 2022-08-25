package ru.magnit.magreportbackend.mapper.filterreport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportField;
import ru.magnit.magreportbackend.dto.request.filterreport.FilterAddRequest;
import ru.magnit.magreportbackend.dto.request.filterreport.FilterFieldAddRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FilterReportMapperTest {

    private static final long ID = 1L;
    private static final String NAME = "Test filter";
    private static final String DESCRIPTION = "Test filter description";
    private static final long ORDINAL = 3L;
    private static final long FILTER_INSTANCE_ID = 4L;

    @InjectMocks
    private FilterReportMapper mapper;

    @Mock
    private FilterReportFieldMapper fieldMapper;

    @Test
    void from() {
        given(fieldMapper.from(ArgumentMatchers.<List<FilterFieldAddRequest>>any())).willReturn(Collections.singletonList(getFilterFields()));

        var result = mapper.from(getSource());
        assertNull(result.getId());
        assertEquals(NAME, result.getName());
        assertEquals(DESCRIPTION, result.getDescription());
        assertTrue(result.isMandatory());
        assertFalse(result.isHidden());
        assertTrue(result.isRootSelectable());
        assertEquals(ORDINAL, result.getOrdinal());
        assertEquals(FILTER_INSTANCE_ID, result.getFilterInstance().getId());
        assertNotNull(result.getFields());
    }

    private FilterReportField getFilterFields() {

        return new FilterReportField();
    }

    private FilterAddRequest getSource() {

        return new FilterAddRequest()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setMandatory(true)
                .setHidden(false)
                .setOrdinal(ORDINAL)
                .setRootSelectable(true)
                .setFields(Collections.emptyList())
                .setFilterInstanceId(FILTER_INSTANCE_ID);
    }
}