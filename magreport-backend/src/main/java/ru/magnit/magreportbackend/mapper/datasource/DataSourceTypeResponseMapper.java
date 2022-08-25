package ru.magnit.magreportbackend.mapper.datasource;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.datasource.DataSourceType;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceTypeResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class DataSourceTypeResponseMapper implements Mapper<DataSourceTypeResponse, DataSourceType> {

    @Override
    public DataSourceTypeResponse from(DataSourceType source) {

        return new DataSourceTypeResponse(
            source.getId(),
            source.getName(),
            source.getDescription(),
            source.getCreatedDateTime(),
            source.getModifiedDateTime());
    }
}
