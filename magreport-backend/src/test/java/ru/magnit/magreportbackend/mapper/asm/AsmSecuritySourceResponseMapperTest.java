package ru.magnit.magreportbackend.mapper.asm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSecurityFilter;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSource;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceField;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceType;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceTypeEnum;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.dto.response.asm.AsmSecurityFilterResponse;
import ru.magnit.magreportbackend.dto.response.asm.AsmSecuritySourceFieldResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetResponse;
import ru.magnit.magreportbackend.mapper.dataset.DataSetResponseMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsmSecuritySourceResponseMapperTest {

    private static final Long ID = 1L;
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final ExternalAuthSourceTypeEnum TYPE_ENUM = ExternalAuthSourceTypeEnum.USER_MAP_SOURCE;
    private static final ExternalAuthSourceType TYPE = new ExternalAuthSourceType(TYPE_ENUM);
    private static final String PRE_SQL = "presql";
    private static final String POST_SQL = "postsql";
    private static final List<ExternalAuthSourceField> FIELDS = Collections.singletonList(null);
    private static final DataSet DATASET = new DataSet(1L);
    private static final List<ExternalAuthSecurityFilter> SECURITY_FILTERS = Collections.singletonList(null);
    private static final LocalDateTime CREATED_DATE = LocalDateTime.now().minusDays(5L);
    private static final LocalDateTime MODIFIED_DATE = LocalDateTime.now();

    @Mock
    private DataSetResponseMapper dataSetResponseMapper;

    @Mock
    private AsmSecuritySourceFieldResponseMapper asmSecuritySourceFieldResponseMapper;

    @Mock
    private AsmSecurityFilterResponseMapper asmSecurityFilterResponseMapper;

    @InjectMocks
    private AsmSecuritySourceResponseMapper mapper;

    @Test
    void from() {
        when(dataSetResponseMapper.from(DATASET)).thenReturn(getDataSetResponse());
        when(asmSecuritySourceFieldResponseMapper.from(anyList())).thenReturn(getFieldsResponse());
        when(asmSecurityFilterResponseMapper.from(anyList())).thenReturn(getFilterResponse());


        final var source = spy(getExternalAuthSource());

        final var result = mapper.from(source);

        assertEquals(ID, result.id());
        assertEquals(NAME, result.name());
        assertEquals(DESCRIPTION, result.description());
        assertEquals(PRE_SQL, result.preSql());
        assertEquals(POST_SQL, result.postSql());
        assertEquals(CREATED_DATE, result.created());
        assertEquals(MODIFIED_DATE, result.modified());
        assertEquals(FIELDS.size(), result.fields().size());
        assertEquals(SECURITY_FILTERS.size(), result.securityFilters().size());
        assertEquals(DATASET.getId(), result.dataSet().getId());
        assertEquals(TYPE_ENUM, result.sourceType());

        verify(source).getId();
        verify(source).getName();
        verify(source).getDescription();
        verify(source).getTypeEnum();
        verify(source).getDataSet();
        verify(source).getPreSql();
        verify(source).getPostSql();
        verify(source).getFields();
        verify(source).getSecurityFilters();
        verify(source).getCreatedDateTime();
        verify(source).getModifiedDateTime();
        verify(dataSetResponseMapper).from(DATASET);
        verify(asmSecuritySourceFieldResponseMapper).from(anyList());
        verify(asmSecurityFilterResponseMapper).from(anyList());

        verifyNoMoreInteractions(source, dataSetResponseMapper, asmSecurityFilterResponseMapper,
                asmSecuritySourceFieldResponseMapper);
    }

    private List<AsmSecurityFilterResponse> getFilterResponse() {
        return Collections.singletonList(null);
    }

    private List<AsmSecuritySourceFieldResponse> getFieldsResponse() {
        return Collections.singletonList(null);
    }

    private DataSetResponse getDataSetResponse() {
        return new DataSetResponse().setId(DATASET.getId());
    }

    private ExternalAuthSource getExternalAuthSource() {
        return new ExternalAuthSource()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setType(TYPE)
                .setPreSql(PRE_SQL)
                .setPostSql(POST_SQL)
                .setFields(FIELDS)
                .setDataSet(DATASET)
                .setSecurityFilters(SECURITY_FILTERS)
                .setCreatedDateTime(CREATED_DATE)
                .setModifiedDateTime(MODIFIED_DATE);
    }
}