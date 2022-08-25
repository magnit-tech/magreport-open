package ru.magnit.magreportbackend.mapper.datasource;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.datasource.DataSource;
import ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceDependenciesResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.auth.UserResponseMapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetDependenciesResponseMapper;

@Service
@RequiredArgsConstructor
public class DataSourceDependenciesResponseMapper implements Mapper<DataSourceDependenciesResponse, DataSource> {

    private final UserResponseMapper userResponseMapper;
    private final DataSetDependenciesResponseMapper dataSetDependenciesResponseMapper;

    @Override
    public DataSourceDependenciesResponse from(DataSource source) {
        return new DataSourceDependenciesResponse(
                source.getId(),
                DataSourceTypeEnum.getByOrdinal(source.getType().getId()),
                source.getName(),
                source.getDescription(),
                source.getUrl(),
                source.getUserName(),
                userResponseMapper.from(source.getUser()),
                dataSetDependenciesResponseMapper.from(source.getDataSets()),
                source.getCreatedDateTime(),
                source.getModifiedDateTime());
    }
}
