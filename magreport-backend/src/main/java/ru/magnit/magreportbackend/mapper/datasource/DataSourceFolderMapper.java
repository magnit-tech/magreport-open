package ru.magnit.magreportbackend.mapper.datasource;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolder;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class DataSourceFolderMapper implements Mapper<DataSourceFolder, FolderAddRequest> {

    @Override
    public DataSourceFolder from(FolderAddRequest source) {
        return mapBaseProperties(source);
    }

    private DataSourceFolder mapBaseProperties(FolderAddRequest source) {
        return new DataSourceFolder()
                .setParentFolder(source.getParentId() == null ? null : new DataSourceFolder(source.getParentId()))
                .setName(source.getName())
                .setDescription(source.getDescription());
    }
}
