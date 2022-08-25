package ru.magnit.magreportbackend.mapper.asm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceFieldFilterInstanceField;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceFieldTypeEnum;
import ru.magnit.magreportbackend.dto.request.asm.AsmSecurityDataSetFieldMapRequest;
import ru.magnit.magreportbackend.dto.request.asm.AsmSecurityFilterInstanceFieldAddRequest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalAuthSourceFieldMapperTest {

    private static final Long ID = 1L;
    private static final ExternalAuthSourceFieldTypeEnum FIELD_TYPE = ExternalAuthSourceFieldTypeEnum.FILTER_VALUE_FIELD;
    private static final Long DATASET_FIELD_ID = 2L;

    @Mock
    private ExternalAuthSourceFieldFilterInstanceFieldMapper fifMapper;

    @InjectMocks
    private ExternalAuthSourceFieldMapper mapper;


    @Test
    void from() {
        when(fifMapper.from(anyList())).thenReturn(Collections.singletonList(mock(ExternalAuthSourceFieldFilterInstanceField.class)));

        final var source = spy(getAsmSecurityDataSetFieldMapRequest());
        final var result = mapper.from(source);

        assertEquals(ID, result.getId());
        assertEquals(FIELD_TYPE, result.getTypeEnum());
        assertEquals(FIELD_TYPE.getId(), result.getType().getId());
        assertEquals(DATASET_FIELD_ID, result.getDataSetField().getId());

        result.getFilterInstanceFields().forEach(f -> verify(f).setSourceField(result));
        result.getFilterInstanceFields().forEach(Mockito::verifyNoMoreInteractions);

        verify(fifMapper).from(source.getFilterInstanceFields());
        verify(source).getId();
        verify(source).getFieldType();
        verify(source).getDataSetFieldId();
        verify(source, times(2)).getFilterInstanceFields();
        verifyNoMoreInteractions(fifMapper, source);
    }

    private AsmSecurityDataSetFieldMapRequest getAsmSecurityDataSetFieldMapRequest() {
        return new AsmSecurityDataSetFieldMapRequest()
                .setId(ID)
                .setFieldType(FIELD_TYPE)
                .setDataSetFieldId(DATASET_FIELD_ID)
                .setFilterInstanceFields(Collections.singletonList(new AsmSecurityFilterInstanceFieldAddRequest()));

    }
}