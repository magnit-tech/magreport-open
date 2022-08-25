package ru.magnit.magreportbackend.mapper.folderreport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.folderreport.Folder;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FolderMapper implements Mapper<Folder, FolderAddRequest> {

    @Override
    public Folder from(FolderAddRequest source) {

        return mapBaseProperties(source);
    }

    private Folder mapBaseProperties(FolderAddRequest source) {

        return new Folder()
                .setParentFolder(source.getParentId() == null ? null : new Folder(source.getParentId()))
                .setName(source.getName())
                .setDescription(source.getDescription());
    }
}
