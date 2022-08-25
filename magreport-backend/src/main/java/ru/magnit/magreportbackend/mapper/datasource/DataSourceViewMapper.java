package ru.magnit.magreportbackend.mapper.datasource;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.datasource.DataSource;
import ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum;
import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class DataSourceViewMapper implements Mapper<DataSourceData, DataSource> {

    @Override
    public DataSourceData from(DataSource source) {
        return mapBaseProperties(source);
    }

    private DataSourceData mapBaseProperties(DataSource source) {
        return new DataSourceData(
                source.getId(),
                DataSourceTypeEnum.getByOrdinal(source.getType().getId()),
                source.getUrl(),
                source.getUserName(),
                source.getPassword(),
                source.getPoolSize());
    }
}
