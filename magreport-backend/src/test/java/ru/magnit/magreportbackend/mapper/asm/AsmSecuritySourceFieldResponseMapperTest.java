package ru.magnit.magreportbackend.mapper.asm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceField;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceFieldFilterInstanceField;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceFieldType;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceFieldTypeEnum;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.dto.response.asm.AsmFilterInstanceFieldResponse;
import ru.magnit.magreportbackend.dto.response.asm.AsmSecuritySourceFieldResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetFieldResponse;
import ru.magnit.magreportbackend.mapper.dataset.DataSetFieldResponseMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsmSecuritySourceFieldResponseMapperTest {

    private static final Long ID = 1L;
    private static final ExternalAuthSourceFieldTypeEnum FIELD_TYPE_ENUM = ExternalAuthSourceFieldTypeEnum.CHANGE_TYPE_FIELD;
    private static final ExternalAuthSourceFieldType FIELD_TYPE = new ExternalAuthSourceFieldType(FIELD_TYPE_ENUM);
    private static final DataSetField DATASET_FIELD = new DataSetField();
    private static final List<ExternalAuthSourceFieldFilterInstanceField> FILTER_INSTANCE_FIELDS
            = Collections.singletonList(new ExternalAuthSourceFieldFilterInstanceField());
    private static final LocalDateTime CREATED_TIME = LocalDateTime.now().minusDays(5);
    private static final LocalDateTime MODIFIED_TIME = LocalDateTime.now();

    @Mock
    DataSetFieldResponseMapper dataSetFieldResponseMapper;

    @Mock
    AsmFilterInstanceFieldResponseMapper fiFieldResponseMapper;

    @Mock
    DataSetFieldResponse dataSetFieldResponse;

    @Mock
    List<AsmFilterInstanceFieldResponse> fiResponse;

    @InjectMocks
    AsmSecuritySourceFieldResponseMapper mapper;

    @Test
    void from() {

        ExternalAuthSourceField source = spy(getExternalAuthSourceField());

        when(dataSetFieldResponseMapper.from(DATASET_FIELD)).thenReturn(dataSetFieldResponse);
        when(fiFieldResponseMapper.from(FILTER_INSTANCE_FIELDS)).thenReturn(fiResponse);

        AsmSecuritySourceFieldResponse result = mapper.from(source);

        assertEquals(ID, result.id());
        assertEquals(FIELD_TYPE_ENUM, result.fieldType());
        assertEquals(fiResponse, result.filterInstanceFields());
        assertEquals(dataSetFieldResponse, result.dataSetField());
        assertEquals(CREATED_TIME, result.created());
        assertEquals(MODIFIED_TIME, result.modified());

        verify(source).getId();
        verify(source).getTypeEnum();
        verify(source).getDataSetField();
        verify(source).getFilterInstanceFields();
        verify(source).getCreatedDateTime();
        verify(source).getModifiedDateTime();

        verify(dataSetFieldResponseMapper).from(DATASET_FIELD);
        verify(fiFieldResponseMapper).from(FILTER_INSTANCE_FIELDS);
        verifyNoMoreInteractions(dataSetFieldResponseMapper, fiFieldResponseMapper, source);
    }

    private ExternalAuthSourceField getExternalAuthSourceField() {
        return new ExternalAuthSourceField()
                .setId(ID)
                .setType(FIELD_TYPE)
                .setDataSetField(DATASET_FIELD)
                .setFilterInstanceFields(FILTER_INSTANCE_FIELDS)
                .setCreatedDateTime(CREATED_TIME)
                .setModifiedDateTime(MODIFIED_TIME);
    }
}