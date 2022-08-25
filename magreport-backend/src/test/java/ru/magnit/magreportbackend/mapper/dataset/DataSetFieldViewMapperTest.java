package ru.magnit.magreportbackend.mapper.dataset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceField;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.domain.dataset.DataType;
import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;
import ru.magnit.magreportbackend.domain.report.ReportField;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterDataSetField;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DataSetFieldViewMapperTest {

    @InjectMocks
    private DataSetFieldViewMapper mapper;

    private final static Long ID = 1L;
    private final static String NAME = "Name";
    private final static String DESCRIPTION = "Description";
    private final static Boolean SYNC = true;
    private final static LocalDateTime CREATE_TIME = LocalDateTime.now();
    private final static LocalDateTime MODIFIED_TIME = LocalDateTime.now();


    @Test
    void from() {

        var response = mapper.from(getDataSetField());

        assertEquals(ID, response.getId());
        assertEquals(DataTypeEnum.STRING, response.getDataType());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(SYNC, response.getValid());

    }


    private DataSetField getDataSetField() {
        return new DataSetField()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setDataSet(new DataSet())
                .setType(new DataType().setId(ID))
                .setIsSync(SYNC)
                .setAuthSourceFields(Collections.singletonList(new ExternalAuthSourceField()))
                .setFieldMappings(Collections.singletonList(new SecurityFilterDataSetField()))
                .setInstanceFields(Collections.singletonList(new FilterInstanceField()))
                .setReportFields(Collections.singletonList(new ReportField()))
                .setCreatedDateTime(CREATE_TIME)
                .setModifiedDateTime(MODIFIED_TIME);

    }
}
