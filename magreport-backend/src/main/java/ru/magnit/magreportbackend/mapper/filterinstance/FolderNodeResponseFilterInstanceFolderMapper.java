package ru.magnit.magreportbackend.mapper.filterinstance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceFolder;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FolderNodeResponseFilterInstanceFolderMapper implements Mapper<FolderNodeResponse, FilterInstanceFolder> {
    @Override
    public FolderNodeResponse from(FilterInstanceFolder source) {
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
