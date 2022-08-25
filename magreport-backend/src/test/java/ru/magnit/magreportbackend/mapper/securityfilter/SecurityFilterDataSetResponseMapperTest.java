package ru.magnit.magreportbackend.mapper.securityfilter;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilter;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterDataSet;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterDataSetField;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetResponse;
import ru.magnit.magreportbackend.exception.NotImplementedException;
import ru.magnit.magreportbackend.mapper.dataset.DataSetResponseMapper;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityFilterDataSetResponseMapperTest {

    private static final Long DATASET_ID = 1L;
    private static final Long ANOTHER_DATASET_ID = 5L;
    private static final Long DATASET_FIELD_ID = 10L;
    private static final Long FILTER_INSTANCE_FIELD = 20L;

    @Mock
    private DataSetResponseMapper dataSetResponseMapper;

    @InjectMocks
    private SecurityFilterDataSetResponseMapper mapper;

    @Test
    void from() {
        assertThrows(NotImplementedException.class, () -> {
            mapper.from(new SecurityFilter());
        });
    }

    @Test
    void fromList() {
        final var dataSet = spy(new DataSet(DATASET_ID));
        final var anotherDataSet = spy(new DataSet(ANOTHER_DATASET_ID));
        final var securityFilterDataSet = spy(new SecurityFilterDataSet().setDataSet(dataSet));

        final var mapping1 = spy(new SecurityFilterDataSetField()
            .setDataSetField(new DataSetField(DATASET_FIELD_ID).setDataSet(dataSet))
            .setFilterInstanceField(new FilterInstanceField(FILTER_INSTANCE_FIELD)));

        final var mapping2 = spy(new SecurityFilterDataSetField()
                .setDataSetField(new DataSetField().setDataSet(anotherDataSet)));

        final var mappings = new ArrayList<SecurityFilterDataSetField>(2);
        mappings.add(mapping1);
        mappings.add(mapping2);


        final var securityFilter = spy(new SecurityFilter());
        securityFilter.setDataSets(Collections.singletonList(securityFilterDataSet));
        securityFilter.setFieldMappings(mappings);
        securityFilterDataSet.setSecurityFilter(securityFilter);

        final var source = Collections.singletonList(securityFilter);

        when(dataSetResponseMapper.from(any(DataSet.class))).thenReturn(new DataSetResponse().setId(DATASET_ID));

        Mockito.reset(securityFilter, securityFilterDataSet);

        final var result = mapper.from(source);

        assertEquals(1, result.size());

        final var entry = result.get(0);
        assertNotNull(entry.dataSet());
        assertEquals(DATASET_ID, entry.dataSet().getId());

        assertNotNull(entry.fields());
        assertEquals(1, entry.fields().size());
        assertEquals(FILTER_INSTANCE_FIELD, entry.fields().get(0).filterInstanceFieldId());
        assertEquals(DATASET_FIELD_ID, entry.fields().get(0).dataSetFieldId());

        verify(securityFilter).getDataSets();
        verify(securityFilterDataSet, times(2)).getDataSet();
        verify(securityFilterDataSet).getSecurityFilter();
        verify(securityFilter).getFieldMappings();
        verify(mapping1, times(2)).getDataSetField();
        verify(mapping1).getFilterInstanceField();
        verify(mapping2).getDataSetField();

        verifyNoMoreInteractions(securityFilter, securityFilterDataSet, mapping1, mapping2);
    }
}