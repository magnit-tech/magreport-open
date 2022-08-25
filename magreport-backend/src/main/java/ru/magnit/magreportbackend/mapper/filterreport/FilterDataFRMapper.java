package ru.magnit.magreportbackend.mapper.filterreport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterreport.FilterReport;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;
import ru.magnit.magreportbackend.dto.inner.filter.FilterData;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceViewMapper;

@Service
@RequiredArgsConstructor
public class FilterDataFRMapper implements Mapper<FilterData, FilterReport> {

    private final DataSourceViewMapper dataSourceMapper;
    private final FilterFieldDataFRMapper fieldMapper;

    @Override
    public FilterData from(FilterReport source) {
        return new FilterData(
                dataSourceMapper.from(source.getFilterInstance().getDataSet().getDataSource()),
                source.getId(),
                FilterTypeEnum.getByOrdinal(source.getFilterInstance().getFilterTemplate().getType().getId()),
                source.getFilterInstance().getDataSet().getSchemaName(),
                source.getFilterInstance().getDataSet().getObjectName(),
                source.getName(),
                source.getCode(),
                source.getDescription(),
                fieldMapper.from(source.getFields()));
    }
}
