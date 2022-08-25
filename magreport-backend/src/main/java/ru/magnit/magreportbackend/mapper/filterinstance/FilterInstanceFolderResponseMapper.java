package ru.magnit.magreportbackend.mapper.filterinstance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceFolder;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceFolderResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FilterInstanceFolderResponseMapper implements Mapper<FilterInstanceFolderResponse, FilterInstanceFolder> {

    private final FilterInstanceResponseMapper responseMapper;

    @Override
    public FilterInstanceFolderResponse from(FilterInstanceFolder source) {
        var folderResponse = mapBaseProperties(source);
        folderResponse.setFilterInstances(responseMapper.from(source.getFilterInstances()));
        folderResponse.setChildFolders(shallowMap(source.getChildFolders()));
        return folderResponse;
    }

    private FilterInstanceFolderResponse mapBaseProperties(FilterInstanceFolder source) {
        return new FilterInstanceFolderResponse()
                .setId(source.getId())
                .setParentId(source.getParentFolder() == null ? null : source.getParentFolder().getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setFilterInstances(responseMapper.from(source.getFilterInstances()))
                .setCreated(source.getCreatedDateTime())
                .setModified(source.getModifiedDateTime());
    }

    @Override
    public FilterInstanceFolderResponse shallowMap(FilterInstanceFolder source) {

        return mapBaseProperties(source);
    }
}
