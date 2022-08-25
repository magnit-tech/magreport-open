package ru.magnit.magreportbackend.mapper.asm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSecurityFilter;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstance;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilter;
import ru.magnit.magreportbackend.dto.inner.dataset.DataSetView;
import ru.magnit.magreportbackend.dto.inner.filter.FilterData;
import ru.magnit.magreportbackend.mapper.dataset.DataSetViewMapper;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterDataFIMapper;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalAuthSecurityFilterViewMapperTest {

    private static final Long ID = 1L;
    private static final Long SECURITY_FILTER_ID = 2L;

    @Mock
    private DataSetViewMapper dataSetViewMapper;

    @Mock
    private FilterDataFIMapper filterDataFIMapper;

    @InjectMocks
    private ExternalAuthSecurityFilterViewMapper mapper;

    @Test
    void from() {
        final var filterInstance = spy(getFilterInstance());
        final var securityFilter = spy(getSecurityFilter());

        final var source = spy(getExternalSecurityFilter());

        when(dataSetViewMapper.from(any(DataSet.class))).thenReturn(new DataSetView());
        when(filterDataFIMapper.from(any(FilterInstance.class))).thenReturn(getFilterData());
        when(source.getSecurityFilter()).thenReturn(securityFilter);
        when(securityFilter.getFilterInstance()).thenReturn(filterInstance);
        when(filterInstance.getDataSet()).thenReturn(new DataSet());

        final var result = mapper.from(source);

        assertEquals(ID, result.getId());
        assertEquals(SECURITY_FILTER_ID, result.getSecurityFilterId());
        assertNotNull(result.getDataSet());
        assertNotNull(result.getFilterInstance());

        verify(source).getId();
        verify(source, times(3)).getSecurityFilter();
        verify(securityFilter, times(2)).getFilterInstance();
        verify(securityFilter).getId();
        verify(filterInstance).getDataSet();
        verify(dataSetViewMapper).from(any(DataSet.class));
        verify(filterDataFIMapper).from(any(FilterInstance.class));

        verifyNoMoreInteractions(source, dataSetViewMapper, filterDataFIMapper, filterInstance, securityFilter);
    }

    private ExternalAuthSecurityFilter getExternalSecurityFilter() {
        return new ExternalAuthSecurityFilter()
                .setId(ID);
    }

    private SecurityFilter getSecurityFilter() {
        return new SecurityFilter()
                .setId(SECURITY_FILTER_ID);
    }

    private FilterInstance getFilterInstance() {
        return new FilterInstance();
    }

    private FilterData getFilterData() {
        return new FilterData(null,
                1L,
                FilterTypeEnum.DATE_RANGE,
                "schema",
                "table",
                "name",
                "code",
                "description",
                Collections.singletonList(null));
    }
}