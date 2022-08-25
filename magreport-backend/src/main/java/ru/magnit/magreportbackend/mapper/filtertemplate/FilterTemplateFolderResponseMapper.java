package ru.magnit.magreportbackend.mapper.filtertemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateFolder;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTemplateFolderResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FilterTemplateFolderResponseMapper implements Mapper<FilterTemplateFolderResponse, FilterTemplateFolder> {

    private final FilterTemplateResponseMapper responseMapper;

    @Override
    public FilterTemplateFolderResponse from(FilterTemplateFolder source) {
        var folderResponse = mapBaseProperties(source);
        folderResponse.setFilterTemplates(responseMapper.from(source.getFilterTemplates()));
        folderResponse.setChildFolders(shallowMap(source.getChildFolders()));

        return folderResponse;
    }

    private FilterTemplateFolderResponse mapBaseProperties(FilterTemplateFolder source) {
        return new FilterTemplateFolderResponse()
                .setId(source.getId())
                .setParentId(source.getParentFolder() == null ? null : source.getParentFolder().getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setCreated(source.getCreatedDateTime())
                .setModified(source.getModifiedDateTime());
    }

    @Override
    public FilterTemplateFolderResponse shallowMap(FilterTemplateFolder source) {

        return mapBaseProperties(source);
    }
}
