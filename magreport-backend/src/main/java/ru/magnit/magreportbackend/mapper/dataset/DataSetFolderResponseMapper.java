package ru.magnit.magreportbackend.mapper.dataset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolder;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetFolderResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class DataSetFolderResponseMapper implements Mapper<DataSetFolderResponse, DataSetFolder> {

    private final DataSetResponseMapper dataSetResponseMapper;

    @Override
    public DataSetFolderResponse from(DataSetFolder source) {
        var folderResponse = mapBaseProperties(source);
        folderResponse.setDataSets(dataSetResponseMapper.from(source.getDataSets()));
        folderResponse.setChildFolders(shallowMap(source.getChildFolders()));
        return folderResponse;
    }

    private DataSetFolderResponse mapBaseProperties(DataSetFolder source) {

        return new DataSetFolderResponse()
                .setId(source.getId())
                .setParentId(source.getParentFolder() == null ? null : source.getParentFolder().getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setCreated(source.getCreatedDateTime())
                .setModified(source.getModifiedDateTime());
    }

    @Override
    public DataSetFolderResponse shallowMap(DataSetFolder source) {

        return mapBaseProperties(source);
    }
}
