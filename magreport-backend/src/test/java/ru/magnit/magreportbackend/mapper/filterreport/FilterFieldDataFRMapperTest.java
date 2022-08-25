package ru.magnit.magreportbackend.mapper.filterreport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.domain.dataset.DataType;
import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportField;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldType;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateField;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
class FilterFieldDataFRMapperTest {

    @InjectMocks
    private FilterFieldDataFRMapper mapper;


    private final static Long ID = 1L;
    private final static String NAME = "Name";
    private final static String DESCRIPTION = "Description";
    private final static Long LEVEL = 1L;
    private final static LocalDateTime CREATE_TIME = LocalDateTime.now();
    private final static LocalDateTime MODIFIED_TIME = LocalDateTime.now();

    @Test
    void from() {
        var response = mapper.from(getFilterReport());

        assertEquals(ID,response.fieldId());
        assertEquals(LEVEL,response.level());
        assertEquals(NAME,response.name());
        assertEquals(DESCRIPTION,response.description());
        assertEquals(NAME,response.fieldName());
        assertEquals(DataTypeEnum.STRING,response.dataType());
        assertEquals(FilterFieldTypeEnum.CODE_FIELD,response.fieldType());
    }

    private FilterReportField getFilterReport() {
        return new FilterReportField()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setFilterInstanceField(
                        new FilterInstanceField()
                                .setTemplateField(
                                        new FilterTemplateField()
                                                .setType(new FilterFieldType().setId(ID)))
                                .setDataSetField(new DataSetField()
                                        .setName(NAME)
                                        .setType(new DataType().setId(ID)))
                                .setLevel(LEVEL))
                .setCreatedDateTime(CREATE_TIME)
                .setModifiedDateTime(MODIFIED_TIME);
    }
}
