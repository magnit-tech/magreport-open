package ru.magnit.magreportbackend.mapper.filterinstance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceFolder;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FilterInstanceFolderMapper implements Mapper<FilterInstanceFolder, FolderAddRequest> {

    @Override
    public FilterInstanceFolder from(FolderAddRequest source) {
        return mapBaseProperties(source);
    }

    private FilterInstanceFolder mapBaseProperties(FolderAddRequest source) {

        return new FilterInstanceFolder()
                .setParentFolder(source.getParentId() == null ? null : new FilterInstanceFolder(source.getParentId()))
                .setName(source.getName())
                .setDescription(source.getDescription());
    }
}
