package ru.magnit.magreportbackend.mapper.asm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceField;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceFieldType;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceFieldTypeEnum;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.dto.inner.asm.ExternalAuthSourceFieldView;
import ru.magnit.magreportbackend.dto.inner.dataset.DataSetFieldView;
import ru.magnit.magreportbackend.mapper.dataset.DataSetFieldViewMapper;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalAuthSourceFieldViewMapperTest {

    private static final Long ID = 1L;
    private static final ExternalAuthSourceFieldTypeEnum TYPE_ENUM = ExternalAuthSourceFieldTypeEnum.FILTER_VALUE_FIELD;
    private static final DataSetField DATASET_FIELD = new DataSetField();

    @Mock
    private DataSetFieldViewMapper dataSetFieldViewMapper;

    @InjectMocks
    private ExternalAuthSourceFieldViewMapper mapper;

    @Test
    void from() {
        final var source = spy(getExternalAuthSourceField());
        when(dataSetFieldViewMapper.from(any(DataSetField.class))).thenReturn(new DataSetFieldView());

        final var result = mapper.from(source);

        assertEquals(ID, result.getId());
        assertEquals(TYPE_ENUM, result.getType());
        assertNotNull(result.getDataSetField());

        verify(source).getId();
        verify(source).getTypeEnum();
        verify(source).getDataSetField();
        verify(dataSetFieldViewMapper).from(any(DataSetField.class));
        verifyNoMoreInteractions(source, dataSetFieldViewMapper);

    }

    @Test
    void mapFrom() {
        final var fieldView = new ExternalAuthSourceFieldView();
        fieldView.setType(TYPE_ENUM);

        final var source = Collections.singletonList(getExternalAuthSourceField());
    }

    private ExternalAuthSourceField getExternalAuthSourceField() {
        return new ExternalAuthSourceField()
                .setId(ID)
                .setType(new ExternalAuthSourceFieldType(TYPE_ENUM))
                .setDataSetField(DATASET_FIELD);
    }
}