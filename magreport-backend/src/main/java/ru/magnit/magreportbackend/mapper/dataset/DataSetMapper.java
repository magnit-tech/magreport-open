package ru.magnit.magreportbackend.mapper.dataset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolder;
import ru.magnit.magreportbackend.domain.dataset.DataSetType;
import ru.magnit.magreportbackend.domain.datasource.DataSource;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class DataSetMapper implements Mapper<DataSet, DataSetAddRequest> {

    private final DataSetFieldMapper dataSetFieldMapper;

    @Override
    public DataSet from(DataSetAddRequest source) {
        var dataSet = mapBaseProperties(source);
        dataSet.setFields(dataSetFieldMapper.from(source.getFields()));
        dataSet.getFields().forEach(field->field.setDataSet(dataSet));

        return dataSet;
    }

    private DataSet mapBaseProperties(DataSetAddRequest source) {
        return new DataSet()
            .setDataSource(new DataSource(source.getDataSourceId()))
            .setName(source.getName())
            .setDescription(source.getDescription())
            .setFolder(new DataSetFolder(source.getFolderId()))
            .setType(new DataSetType(source.getTypeId()))
            .setCatalogName(source.getCatalogName())
            .setSchemaName(source.getSchemaName())
            .setObjectName(source.getObjectName());
    }
}
