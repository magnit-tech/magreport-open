package ru.magnit.magreportbackend.mapper.securityfilter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterFolder;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class SecurityFilterFolderMapper implements Mapper<SecurityFilterFolder, FolderAddRequest> {

    @Override
    public SecurityFilterFolder from(FolderAddRequest source) {
        return mapBaseProperties(source);
    }

    private SecurityFilterFolder mapBaseProperties(FolderAddRequest source) {
        return new SecurityFilterFolder()
                .setParentFolder(source.getParentId() == null ? null : new SecurityFilterFolder(source.getParentId()))
                .setName(source.getName())
                .setDescription(source.getDescription());
    }

}
