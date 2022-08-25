package ru.magnit.magreportbackend.mapper.folderentitty;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.FolderEntity;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FolderNodeResponseFolderEntityMapper implements Mapper<FolderNodeResponse, FolderEntity> {
    @Override
    public FolderNodeResponse from(FolderEntity source) {
        return new FolderNodeResponse (
                source.getId(),
                source.getParentFolder() == null ? null : source.getParentFolder().getId(),
                source.getName(),
                source.getDescription(),
                source.getCreatedDateTime(),
                source.getModifiedDateTime()
        );
    }
}
