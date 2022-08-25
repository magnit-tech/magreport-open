package ru.magnit.magreportbackend.mapper.dataset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetAddRequest;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetCreateFromMetaDataRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class DataSetAddRequestMapper implements Mapper<DataSetAddRequest, DataSetCreateFromMetaDataRequest> {

    @Override
    public DataSetAddRequest from(DataSetCreateFromMetaDataRequest source) {
        return mapBaseProperties(source);
    }

    private DataSetAddRequest mapBaseProperties(DataSetCreateFromMetaDataRequest source) {

        return new DataSetAddRequest()
                .setDataSourceId(source.getDataSourceId())
                .setId(source.getDataSourceId())
                .setFolderId(source.getFolderId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setCatalogName(source.getCatalogName())
                .setSchemaName(source.getSchemaName())
                .setObjectName(source.getObjectName())
                .setTypeId(source.getTypeId());
    }
}
