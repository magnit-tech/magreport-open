package ru.magnit.magreportbackend.mapper.datasource;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.datasource.DataSource;
import ru.magnit.magreportbackend.domain.datasource.DataSourceType;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceTypeResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class DataSourceResponseMapper implements Mapper<DataSourceResponse, DataSource> {

    private final Mapper<DataSourceTypeResponse, DataSourceType> dataSourceTypeResponseMapper;

    @Override
    public DataSourceResponse from(DataSource source) {
        return mapBaseProperties(source);
    }

    private DataSourceResponse mapBaseProperties(DataSource source) {
        return new DataSourceResponse(
            source.getId(),
            source.getName(),
            source.getDescription(),
            source.getUrl(),
            source.getUserName(),
            dataSourceTypeResponseMapper.from(source.getType()),
            source.getPoolSize(),
            source.getUser().getName(),
            source.getCreatedDateTime(),
            source.getModifiedDateTime()
        );
    }
}
