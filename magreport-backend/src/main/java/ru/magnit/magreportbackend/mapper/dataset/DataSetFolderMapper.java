package ru.magnit.magreportbackend.mapper.dataset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolder;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class DataSetFolderMapper implements Mapper<DataSetFolder, FolderAddRequest> {

    @Override
    public DataSetFolder from(FolderAddRequest source) {
        return mapBaseProperties(source);
    }

    private DataSetFolder mapBaseProperties(FolderAddRequest source) {
        return new DataSetFolder()
                .setParentFolder(source.getParentId() == null ? null : new DataSetFolder(source.getParentId()))
                .setName(source.getName())
                .setDescription(source.getDescription());
    }
}