package ru.magnit.magreportbackend.mapper.dataset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetFieldResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceResponseMapper;

@Service
@RequiredArgsConstructor
public class DataSetResponseMapper implements Mapper<DataSetResponse, DataSet> {

    private final DataSetFieldResponseMapper fieldResponseMapper;
    private final DataSourceResponseMapper dataSourceResponseMapper;

    @Override
    public DataSetResponse from(DataSet source) {
        return mapBaseProperties(source);
    }

    private DataSetResponse mapBaseProperties(DataSet source) {
        final var dataSetResponse = new DataSetResponse()
                .setId(source.getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setCatalogName(source.getCatalogName())
                .setSchemaName(source.getSchemaName())
                .setObjectName(source.getObjectName())
                .setTypeId(source.getType().getId())
                .setDataSource(dataSourceResponseMapper.from(source.getDataSource()))
                .setFields(fieldResponseMapper.from(source.getFields()))
                .setUserName(source.getUser().getName())
                .setCreated(source.getCreatedDateTime())
                .setModified(source.getModifiedDateTime());
        dataSetResponse.setIsValid(dataSetResponse.getFields().stream().allMatch(DataSetFieldResponse::getIsValid));

        return dataSetResponse;
    }
}
