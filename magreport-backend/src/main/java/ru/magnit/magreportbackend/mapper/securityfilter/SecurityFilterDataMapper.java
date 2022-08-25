package ru.magnit.magreportbackend.mapper.securityfilter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilter;
import ru.magnit.magreportbackend.dto.inner.filter.FilterData;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceViewMapper;

@Service
@RequiredArgsConstructor
public class SecurityFilterDataMapper implements Mapper<FilterData, SecurityFilter> {

    private final DataSourceViewMapper dataSourceMapper;
    private final SecurityFilterFieldDataMapper fieldMapper;

    @Override
    public FilterData from(SecurityFilter source) {
        return new FilterData(
                dataSourceMapper.from(source.getFilterInstance().getDataSet().getDataSource()),
                source.getId(),
                FilterTypeEnum.getByOrdinal(source.getFilterInstance().getFilterTemplate().getType().getId()),
                source.getFilterInstance().getDataSet().getSchemaName(),
                source.getFilterInstance().getDataSet().getObjectName(),
                source.getName(),
               "",
                source.getDescription(),
                fieldMapper.from(source.getFieldMappings())
        );
    }
}
