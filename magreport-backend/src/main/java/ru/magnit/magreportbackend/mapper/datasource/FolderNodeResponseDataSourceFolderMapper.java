package ru.magnit.magreportbackend.mapper.datasource;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolder;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FolderNodeResponseDataSourceFolderMapper implements Mapper<FolderNodeResponse, DataSourceFolder> {
    @Override
    public FolderNodeResponse from(DataSourceFolder source) {
        return new FolderNodeResponse(
                source.getId(),
                source.getParentFolder() != null ? source.getParentFolder().getId() : null,
                source.getName(),
                source.getDescription(),
                source.getCreatedDateTime(),
                source.getModifiedDateTime()
        );
    }
}
