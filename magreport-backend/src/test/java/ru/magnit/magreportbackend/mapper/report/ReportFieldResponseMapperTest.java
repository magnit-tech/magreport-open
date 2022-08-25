package ru.magnit.magreportbackend.mapper.report;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.domain.report.PivotFieldType;
import ru.magnit.magreportbackend.domain.report.ReportField;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportFieldResponseMapperTest {

    private static final Long ID = 1L;
    private static final String NAME = "name";
    private static final String DESCRIPTION = "desc";
    private static final Long DATASET_FIELD_ID = 2L;
    private static final Boolean DATASET_FIELD_IS_SYNC = false;
    private static final DataSetField DATASET_FIELD = spy(new DataSetField().setId(DATASET_FIELD_ID).setIsSync(DATASET_FIELD_IS_SYNC));
    private static final Integer ORDINAL = 123;
    private static final Boolean VISIBLE = true;
    private static final Long PIVOT_FIELD_TYPE_ID = 5L;
    private static final PivotFieldType PIVOT_FIELD_TYPE = spy(new PivotFieldType(PIVOT_FIELD_TYPE_ID));
    private static final LocalDateTime CREATED_DATE = LocalDateTime.now().minusDays(5L);
    private static final LocalDateTime MODIFIED_DATE = LocalDateTime.now();

    @InjectMocks
    private ReportFieldResponseMapper mapper;

    @Test
    void from() {

        final var source = spy(getReportField());

        final var result = mapper.from(source);

        assertEquals(ID, result.getId());
        assertEquals(NAME, result.getName());
        assertEquals(DESCRIPTION, result.getDescription());
        assertEquals(DATASET_FIELD_ID, result.getDataSetFieldId());
        assertEquals(ORDINAL, result.getOrdinal());
        assertEquals(VISIBLE, result.getVisible());
        assertEquals(PIVOT_FIELD_TYPE_ID, result.getPivotTypeId());
        assertEquals(DATASET_FIELD_IS_SYNC, result.getValid());
        assertEquals(CREATED_DATE, result.getCreated());
        assertEquals(MODIFIED_DATE, result.getModified());

        verify(source).getId();
        verify(source).getName();
        verify(source).getDescription();
        verify(source, times(2)).getDataSetField();
        verify(source).getOrdinal();
        verify(source).getVisible();
        verify(source).getPivotFieldType();
        verify(source).getCreatedDateTime();
        verify(source).getModifiedDateTime();
        verify(DATASET_FIELD).getId();
        verify(DATASET_FIELD).getIsSync();
        verify(PIVOT_FIELD_TYPE).getId();
        verifyNoMoreInteractions(source, DATASET_FIELD, PIVOT_FIELD_TYPE);
    }

    private ReportField getReportField() {
        return new ReportField()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setDataSetField(DATASET_FIELD)
                .setOrdinal(ORDINAL)
                .setVisible(VISIBLE)
                .setPivotFieldType(PIVOT_FIELD_TYPE)
                .setCreatedDateTime(CREATED_DATE)
                .setModifiedDateTime(MODIFIED_DATE);
    }
}