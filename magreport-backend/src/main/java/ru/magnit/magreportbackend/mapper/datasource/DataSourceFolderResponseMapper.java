package ru.magnit.magreportbackend.mapper.datasource;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolder;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceFolderResponse;
import ru.magnit.magreportbackend.mapper.Mapper;


@Service
@RequiredArgsConstructor
public class DataSourceFolderResponseMapper implements Mapper<DataSourceFolderResponse, DataSourceFolder> {

    private final DataSourceResponseMapper dataSourceResponseMapper;

    @Override
    public DataSourceFolderResponse from(DataSourceFolder source) {

        var folderResponse = mapBaseProperties(source);
        folderResponse.setDataSources(dataSourceResponseMapper.from(source.getDataSources()));
        folderResponse.setChildFolders(shallowMap(source.getChildFolders()));
        return folderResponse;
    }

    @Override
    public DataSourceFolderResponse shallowMap(DataSourceFolder source) {

        return mapBaseProperties(source);
    }

    private DataSourceFolderResponse mapBaseProperties(DataSourceFolder source) {

        return new DataSourceFolderResponse()
                .setId(source.getId())
                .setParentId(source.getParentFolder() == null ? null : source.getParentFolder().getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setCreated(source.getCreatedDateTime())
                .setModified(source.getModifiedDateTime());
    }
}
