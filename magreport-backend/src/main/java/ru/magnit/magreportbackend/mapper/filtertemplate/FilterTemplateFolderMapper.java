package ru.magnit.magreportbackend.mapper.filtertemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateFolder;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FilterTemplateFolderMapper implements Mapper<FilterTemplateFolder, FolderAddRequest> {

    @Override
    public FilterTemplateFolder from(FolderAddRequest source) {
        return mapBaseProperties(source);
    }

    private FilterTemplateFolder mapBaseProperties(FolderAddRequest source) {
        return new FilterTemplateFolder()
                .setParentFolder(source.getParentId() == null ? null : new FilterTemplateFolder(source.getParentId()))
                .setName(source.getName())
                .setDescription(source.getDescription());
    }
}
