package ru.magnit.magreportbackend.mapper.filtertemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateFolder;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FolderNodeResponseFilterTemplateFolderMapper  implements Mapper<FolderNodeResponse, FilterTemplateFolder> {
    @Override
    public FolderNodeResponse from(FilterTemplateFolder source) {
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
