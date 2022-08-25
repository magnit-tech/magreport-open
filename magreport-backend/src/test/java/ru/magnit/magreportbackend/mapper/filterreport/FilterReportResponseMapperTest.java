package ru.magnit.magreportbackend.mapper.filterreport;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstance;
import ru.magnit.magreportbackend.domain.filterreport.FilterReport;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportField;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportGroup;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplate;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterType;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportFieldResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FilterReportResponseMapperTest {

    private static final Long ID = 1L;
    private static final String NAME = "Test filter report";
    private static final String DESCRIPTION = "Test filter report description";
    private static final Integer ORDINAL = 2;
    private static final Long GROUP_ID = 3L;
    private static final Long FILTER_INSTANCE_ID = 4L;
    private static final LocalDateTime CREATED_DATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_DATE_TIME = CREATED_DATE_TIME.plusMinutes(2);
    private static final Long FIELD_ID = 11L;
    private static final String FIELD_NAME = "Test report field";
    private static final String FIELD_DESCRIPTION = "Test report field description";
    private static final Long FILTER_INSTANCE_FIELD_ID = 5L;
    private static final Long REPORT_FIELD_ID = 6L;
    private static final long FILTER_TYPE = 1L;

    @InjectMocks
    private FilterReportResponseMapper mapper;

    @Mock
    private FilterReportFieldResponseMapper fieldMapper;

    @Test
    void from() {

        given(fieldMapper.from(ArgumentMatchers.<List<FilterReportField>>any())).willReturn(Collections.singletonList(getFilterFields()));

        var result  = mapper.from(getSource());

        assertEquals(ID, result.id());
        assertEquals(NAME, result.name());
        assertEquals(DESCRIPTION, result.description());
        assertEquals(ORDINAL, result.ordinal());
        assertEquals(FIELD_ID, result.fields().get(0).id());
        assertEquals(FIELD_NAME, result.fields().get(0).name());
        assertEquals(FIELD_DESCRIPTION, result.fields().get(0).description());
    }

    private FilterReportFieldResponse getFilterFields() {

        return new FilterReportFieldResponse(
                FIELD_ID,
                FIELD_NAME,
                FIELD_DESCRIPTION,
                FilterFieldTypeEnum.CODE_FIELD,
                1L,
                1L,
                FILTER_INSTANCE_FIELD_ID,
                REPORT_FIELD_ID,
                CREATED_DATE_TIME,
                MODIFIED_DATE_TIME,
                true);
    }

    private FilterReport getSource() {

        return new FilterReport()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setOrdinal(ORDINAL.longValue())
                .setMandatory(true)
                .setHidden(false)
                .setRootSelectable(false)
                .setGroup(new FilterReportGroup(GROUP_ID))
                .setFilterInstance(new FilterInstance(FILTER_INSTANCE_ID).setFilterTemplate(new FilterTemplate(FILTER_INSTANCE_ID).setType(new FilterType(FILTER_TYPE))))
                .setUser(new User())
                .setCreatedDateTime(CREATED_DATE_TIME)
                .setModifiedDateTime(MODIFIED_DATE_TIME);
    }
}