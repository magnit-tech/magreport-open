package ru.magnit.magreportbackend.mapper.reportjob;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportGroup;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.report.ReportField;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportFieldData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportFilterGroupData;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReportDataMapperTest {

    private static final long ID = 1L;
    private static final String NAME = "Test report";
    private static final String DESCRIPTION = "Test report description";
    private static final String LINK = "some link";
    private static final LocalDateTime CREATED_DATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_DATE_TIME = LocalDateTime.now();
    private static final long FIELD_ID = 4L;
    private static final String FIELD_NAME = "Test field";
    private static final String FIELD_DESCRIPTION = "Test field description";
    private static final int ORDINAL = 5;
    private static final String SCHEMA_NAME = "Test schema";
    private static final String OBJECT_NAME = "Test object";

    @InjectMocks
    private ReportDataMapper mapper;

    @Mock
    private ReportFieldDataMapper reportFieldMapper;

    @Mock
    private ReportFilterGroupDataMapper filtersMapper;

    @Test
    void from() {
        given(reportFieldMapper.from(ArgumentMatchers.<List<ReportField>>any())).willReturn(Collections.singletonList(getReportField()));
        given(filtersMapper.from(ArgumentMatchers.<FilterReportGroup>any())).willReturn(new ReportFilterGroupData(1L,1L,null, null,null, null, null));

        var result = mapper.from(getSource());

        assertEquals(ID, result.id());
        assertEquals(NAME, result.name());
        assertEquals(DESCRIPTION, result.description());
        assertEquals(SCHEMA_NAME, result.schemaName());
        assertEquals(OBJECT_NAME, result.tableName());
        assertEquals(FIELD_ID, result.fields().get(0).id());
        assertEquals(FIELD_NAME, result.fields().get(0).name());
        assertEquals(FIELD_NAME, result.fields().get(0).columnName());
        assertEquals(FIELD_DESCRIPTION, result.fields().get(0).description());
        assertEquals(ORDINAL, result.fields().get(0).ordinal());
        assertEquals(DataTypeEnum.STRING, result.fields().get(0).dataType());
    }

    private ReportFieldData getReportField() {

        return new ReportFieldData(
                FIELD_ID,
                ORDINAL,
                true,
                DataTypeEnum.STRING,
                FIELD_NAME,
                FIELD_NAME,
                FIELD_DESCRIPTION
        );
    }

    private Report getSource() {

        return new Report()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setDataSet(new DataSet().setSchemaName(SCHEMA_NAME).setObjectName(OBJECT_NAME))
                .setRequirementsLink(LINK)
                .setFields(Collections.singletonList(new ReportField(ID).setName("field1").setOrdinal(1).setVisible(true)))
                .setCreatedDateTime(CREATED_DATE_TIME)
                .setModifiedDateTime(MODIFIED_DATE_TIME);
    }
}