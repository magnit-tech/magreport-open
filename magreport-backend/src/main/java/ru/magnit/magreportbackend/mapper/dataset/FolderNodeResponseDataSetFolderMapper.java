package ru.magnit.magreportbackend.mapper.dataset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolder;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FolderNodeResponseDataSetFolderMapper implements Mapper<FolderNodeResponse, DataSetFolder> {
    @Override
    public FolderNodeResponse from(DataSetFolder source) {
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
