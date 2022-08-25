package ru.magnit.magreportbackend.mapper.dataset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.mapper.Cloner;

@Service
@RequiredArgsConstructor
public class DataSetCloner implements Cloner<DataSet> {

    private final DataSetFieldCloner fieldCloner;

    @Override
    public DataSet clone(DataSet source) {
        final var  dataSet = new DataSet()
            .setType(source.getType())
            .setName(source.getName())
            .setDescription(source.getDescription())
            .setDataSource(source.getDataSource())
            .setFolder(source.getFolder())
            .setObjectName(source.getObjectName())
            .setSchemaName(source.getSchemaName())
            .setCatalogName(source.getCatalogName());

        final var fields = fieldCloner.clone(source.getFields());
        fields.forEach(field -> field.setDataSet(dataSet));
        dataSet.setFields(fields);

        return dataSet;
    }
}
