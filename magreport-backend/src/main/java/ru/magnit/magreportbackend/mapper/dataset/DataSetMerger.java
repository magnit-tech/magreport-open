package ru.magnit.magreportbackend.mapper.dataset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolder;
import ru.magnit.magreportbackend.domain.dataset.DataSetType;
import ru.magnit.magreportbackend.domain.datasource.DataSource;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetAddRequest;
import ru.magnit.magreportbackend.mapper.Merger;

@Service
@RequiredArgsConstructor
public class DataSetMerger implements Merger<DataSet, DataSetAddRequest> {

    private final DataSetFieldMerger fieldMerger;

    @Override
    public DataSet merge(DataSet target, DataSetAddRequest source) {
        var dataSet = target
                .setDataSource(new DataSource(source.getDataSourceId()))
                .setFolder(new DataSetFolder(source.getFolderId()))
                .setType(new DataSetType(source.getTypeId()))
                .setCatalogName(source.getCatalogName())
                .setSchemaName(source.getSchemaName())
                .setObjectName(source.getObjectName())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setFields(fieldMerger.merge(target.getFields(), source.getFields()));

        dataSet.getFields().forEach(field -> field.setDataSet(dataSet));

        return dataSet;
    }
}
