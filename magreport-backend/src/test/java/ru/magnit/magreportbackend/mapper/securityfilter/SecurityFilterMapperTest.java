package ru.magnit.magreportbackend.mapper.securityfilter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationTypeEnum;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterDataSet;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterDataSetField;
import ru.magnit.magreportbackend.dto.request.securityfilter.FieldMapping;
import ru.magnit.magreportbackend.dto.request.securityfilter.SecurityFilterAddRequest;
import ru.magnit.magreportbackend.dto.request.securityfilter.SecurityFilterDataSetAddRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityFilterMapperTest {

    private static final String NAME = "name";
    private static final String DESCRIPTION = "desc";
    private static final FilterOperationTypeEnum OPERATION_TYPE = FilterOperationTypeEnum.IS_BETWEEN;
    private static final Long FOLDER_ID = 1L;
    private static final Long FILTER_INSTANCE_ID = 5L;
    private static final FieldMapping DATASET_FIELD = new FieldMapping();
    private static final SecurityFilterDataSetAddRequest DATASET = new SecurityFilterDataSetAddRequest().setFields(Collections.singletonList(DATASET_FIELD));
    private static final List<SecurityFilterDataSetAddRequest> DATASETS = Collections.singletonList(DATASET);

    @Mock
    private SecurityFilterDataSetFieldMapper fieldMapper;

    @Mock
    private SecurityFilterDataSetMapper dataSetMapper;

    @InjectMocks
    private SecurityFilterMapper mapper;

    @Test
    void from() {

        final var source = spy(getSecurityFilterAddRequest());

        when(dataSetMapper.from(anyList())).thenReturn(Collections.singletonList(new SecurityFilterDataSet()));
        when(fieldMapper.from(anyList())).thenReturn(Collections.singletonList(new SecurityFilterDataSetField()));

        final var result = mapper.from(source);

        assertEquals(OPERATION_TYPE, result.getOperationType().getTypeEnum());
        assertEquals(NAME, result.getName());
        assertEquals(DESCRIPTION, result.getDescription());
        assertEquals(FOLDER_ID, result.getFolder().getId());
        assertEquals(FILTER_INSTANCE_ID, result.getFilterInstance().getId());
        assertNotNull(result.getDataSets());
        assertEquals(1, result.getDataSets().size());
        assertEquals(1, result.getFieldMappings().size());


        result.getDataSets().forEach(sf -> assertEquals(result, sf.getSecurityFilter()));
        result.getFieldMappings().forEach(fm -> assertEquals(result, fm.getSecurityFilter()));

        verify(source).getOperationType();
        verify(source).getName();
        verify(source).getDescription();
        verify(source).getFolderId();
        verify(source).getFilterInstanceId();
        verify(source, times(2)).getDataSets();

        verifyNoMoreInteractions(source, fieldMapper, dataSetMapper);

    }

    private SecurityFilterAddRequest getSecurityFilterAddRequest() {
        return new SecurityFilterAddRequest()
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setOperationType(OPERATION_TYPE)
                .setFolderId(FOLDER_ID)
                .setFilterInstanceId(FILTER_INSTANCE_ID)
                .setDataSets(DATASETS);
    }
}