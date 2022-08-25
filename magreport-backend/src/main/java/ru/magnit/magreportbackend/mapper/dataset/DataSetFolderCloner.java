package ru.magnit.magreportbackend.mapper.dataset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolder;
import ru.magnit.magreportbackend.mapper.Cloner;

@Service
@RequiredArgsConstructor
public class DataSetFolderCloner implements Cloner<DataSetFolder> {

    private final DataSetCloner dataSetCloner;
    private final DataSetFolderRoleCloner dataSetFolderRoleCloner;

    @Override
    public DataSetFolder clone(DataSetFolder source) {
        final var datasetFolder = new DataSetFolder()
                .setName(source.getName())
                .setDescription(source.getDescription());

        final var datasets = dataSetCloner.clone(source.getDataSets());
        datasets.forEach(dataset -> dataset.setFolder(datasetFolder));
        datasetFolder.setDataSets(datasets);

        final var folderRoles = dataSetFolderRoleCloner.clone(source.getFolderRoles());
        folderRoles.forEach(f -> f.setFolder(datasetFolder));
        datasetFolder.setFolderRoles(folderRoles);

        return datasetFolder;
    }
}
