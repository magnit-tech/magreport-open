package ru.magnit.magreportbackend.mapper.reportjob;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.domain.dataset.DataType;
import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.domain.report.PivotFieldType;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.report.ReportField;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
class ReportFieldDataMapperTest {

    private static final long ID = 1L;
    private static final String NAME = "Test report field";
    private static final String DESCRIPTION = "Test report field description";
    private static final int ORDINAL = 2;
    private static final boolean VISIBLE = true;
    private static final long REPORT_ID = 3L;
    private static final long DATASET_FIELD_ID = 4L;
    private static final String FIELD_NAME = "DATASET_FIELD";

    @InjectMocks
    private ReportFieldDataMapper mapper;

    @Test
    void from() {

        var result = mapper.from(getSource());

        assertEquals(ID, result.id());
        assertEquals(NAME, result.name());
        assertEquals(DESCRIPTION, result.description());
        assertEquals(FIELD_NAME, result.columnName());
        assertEquals(DataTypeEnum.INTEGER.ordinal(), result.dataType().ordinal());
        assertEquals(ORDINAL, result.ordinal());
    }

    private ReportField getSource() {

        return new ReportField()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setOrdinal(ORDINAL)
                .setVisible(VISIBLE)
                .setPivotFieldType(new PivotFieldType())
                .setReport(new Report(REPORT_ID))
                .setDataSetField(new DataSetField(DATASET_FIELD_ID).setName(FIELD_NAME).setType(new DataType(DataTypeEnum.INTEGER)));
    }
}