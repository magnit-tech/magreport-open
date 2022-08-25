package ru.magnit.magreportbackend.mapper.filterreport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportField;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldType;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateField;
import ru.magnit.magreportbackend.domain.report.ReportField;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class FilterReportFieldResponseMapperTest {

    private static final long ID = 1L;
    private static final String NAME = "Test report field";
    private static final String DESCRIPTION = "Test report field description";
    private static final long FILTER_INSTANCE_FIELD_ID = 2L;
    private static final long REPORT_FIELD_ID = 3L;
    private static final LocalDateTime CREATED_DATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_DATE_TIME = CREATED_DATE_TIME.plusMinutes(2L);

    @InjectMocks
    private FilterReportFieldResponseMapper mapper;

    @Test
    void from() {

        var result = mapper.from(getSource());

        assertEquals(ID, result.id());
        assertEquals(NAME, result.name());
        assertEquals(DESCRIPTION, result.description());
        assertEquals(REPORT_FIELD_ID, result.reportFieldId());
        assertEquals(FILTER_INSTANCE_FIELD_ID, result.filterInstanceFieldId());
        assertEquals(CREATED_DATE_TIME, result.created());
        assertEquals(MODIFIED_DATE_TIME, result.modified());
    }

    private FilterReportField getSource() {

        return new FilterReportField()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setFilterInstanceField(new FilterInstanceField(FILTER_INSTANCE_FIELD_ID).setTemplateField(new FilterTemplateField(ID).setType(new FilterFieldType(FilterFieldTypeEnum.CODE_FIELD))))
                .setReportField(new ReportField(REPORT_FIELD_ID))
                .setCreatedDateTime(CREATED_DATE_TIME)
                .setModifiedDateTime(MODIFIED_DATE_TIME);
    }
}