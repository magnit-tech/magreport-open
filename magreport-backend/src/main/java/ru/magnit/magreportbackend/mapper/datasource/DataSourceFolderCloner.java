package ru.magnit.magreportbackend.mapper.datasource;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolder;
import ru.magnit.magreportbackend.mapper.Cloner;

@Service
@RequiredArgsConstructor
public class DataSourceFolderCloner implements Cloner<DataSourceFolder> {

    private final DataSourceCloner dataSourceCloner;
    private final DataSourceFolderRoleCloner dataSourceFolderRoleCloner;

    @Override
    public DataSourceFolder clone(DataSourceFolder source) {
        final DataSourceFolder folder = new DataSourceFolder()
                .setName(source.getName())
                .setDescription(source.getDescription());

        final var dataSources = dataSourceCloner.clone(source.getDataSources());
        dataSources.forEach(dataSource -> dataSource.setFolder(folder));
        folder.setDataSources(dataSources);

        final var folderRoles = dataSourceFolderRoleCloner.clone(source.getFolderRoles());
        folderRoles.forEach(f -> f.setFolder(folder));
        folder.setFolderRoles(folderRoles);

        return folder;

    }
}
