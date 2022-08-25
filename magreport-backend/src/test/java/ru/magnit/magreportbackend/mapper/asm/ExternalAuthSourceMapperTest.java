package ru.magnit.magreportbackend.mapper.asm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSecurityFilter;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceField;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceTypeEnum;
import ru.magnit.magreportbackend.dto.request.asm.AsmSecurityDataSetFieldMapRequest;
import ru.magnit.magreportbackend.dto.request.asm.AsmSecuritySecurityFilterAddRequest;
import ru.magnit.magreportbackend.dto.request.asm.AsmSecuritySourceAddRequest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalAuthSourceMapperTest {
    public static final ExternalAuthSourceTypeEnum SOURCE_TYPE = ExternalAuthSourceTypeEnum.GROUP_SOURCE;
    public static final String NAME = "Security source name";
    public static final String DESCRIPTION = "Security source description";
    public static final long DATASET_ID = 1L;
    public static final long FILTER_ID = 2L;
    public static final String PRE_SQL = "pre sql";
    public static final String POST_SQL = "post sql";

    @Mock
    private ExternalAuthSourceFieldMapper sourceFieldMapper;

    @Mock
    private ExternalAuthSecurityFilterMapper securityFilterMapper;

    @InjectMocks
    private ExternalAuthSourceMapper mapper;

    @Test
    void from() {
        AsmSecuritySourceAddRequest request = spy(getAsmSecuritySourceAddRequest());

        when(sourceFieldMapper.from(anyList())).thenReturn(Collections.singletonList(mock(ExternalAuthSourceField.class)));
        when(securityFilterMapper.from(anyList())).thenReturn(Collections.singletonList(mock(ExternalAuthSecurityFilter.class)));

        final var result = mapper.from(request);

        assertEquals(SOURCE_TYPE, result.getTypeEnum());
        assertEquals(NAME, result.getName());
        assertEquals(DESCRIPTION, result.getDescription());
        assertEquals(DATASET_ID, result.getDataSet().getId());
        assertEquals(PRE_SQL, result.getPreSql());
        assertEquals(POST_SQL, result.getPostSql());
        assertNotNull(result.getFields());
        assertNotNull(result.getSecurityFilters());

        result.getFields().forEach(field -> verify(field).setSource(result));
        result.getFields().forEach(Mockito::verifyNoMoreInteractions);

        result.getSecurityFilters().forEach(sf -> verify(sf).setSource(result));
        result.getSecurityFilters().forEach(Mockito::verifyNoMoreInteractions);

        verify(sourceFieldMapper).from(request.getFields());
        verify(securityFilterMapper).from(request.getSecurityFilters());
        verify(request).getId();
        verify(request).getSourceType();
        verify(request).getName();
        verify(request).getDescription();
        verify(request).getPreSql();
        verify(request).getPostSql();
        verify(request).getDataSetId();
        verify(request, times(2)).getFields();
        verify(request, times(2)).getSecurityFilters();
        verifyNoMoreInteractions(request, sourceFieldMapper);
    }

    private AsmSecuritySourceAddRequest getAsmSecuritySourceAddRequest() {
        return new AsmSecuritySourceAddRequest()
                .setSourceType(SOURCE_TYPE)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setDataSetId(DATASET_ID)
                .setSecurityFilters(Collections.singletonList(new AsmSecuritySecurityFilterAddRequest()))
                .setFields(Collections.singletonList(new AsmSecurityDataSetFieldMapRequest()))
                .setPreSql(PRE_SQL)
                .setPostSql(POST_SQL);
    }
}