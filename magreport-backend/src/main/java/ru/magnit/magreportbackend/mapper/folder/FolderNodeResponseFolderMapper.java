package ru.magnit.magreportbackend.mapper.folder;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.folderreport.Folder;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FolderNodeResponseFolderMapper implements Mapper<FolderNodeResponse, Folder> {

    @Override
    public FolderNodeResponse from(Folder source) {

        return new FolderNodeResponse(
                source.getId(),
                source.getParentFolder() == null ? null : source.getParentFolder().getId(),
                source.getName(),
                source.getDescription(),
                source.getCreatedDateTime(),
                source.getModifiedDateTime());
    }
}
