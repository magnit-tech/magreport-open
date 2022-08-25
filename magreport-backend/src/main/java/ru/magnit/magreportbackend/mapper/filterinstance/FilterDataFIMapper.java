package ru.magnit.magreportbackend.mapper.filterinstance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstance;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;
import ru.magnit.magreportbackend.dto.inner.filter.FilterData;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceViewMapper;

@Service
@RequiredArgsConstructor
public class FilterDataFIMapper implements Mapper<FilterData, FilterInstance> {

    private final DataSourceViewMapper dataSourceMapper;
    private final FilterFieldDataFIMapper fieldMapper;

    @Override
    public FilterData from(FilterInstance source) {
        return new FilterData(
                source.getDataSet() == null ? null : dataSourceMapper.from(source.getDataSet().getDataSource()),
                source.getId(),
                FilterTypeEnum.getByOrdinal(source.getFilterTemplate().getType().getId()),
                source.getDataSet() == null ? null : source.getDataSet().getSchemaName(),
                source.getDataSet() == null ? null : source.getDataSet().getObjectName(),
                source.getName(),
                source.getCode(),
                source.getDescription(),
                fieldMapper.from(source.getFields()));
    }
}
