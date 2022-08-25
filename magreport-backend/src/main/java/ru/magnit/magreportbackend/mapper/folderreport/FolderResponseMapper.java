package ru.magnit.magreportbackend.mapper.folderreport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.folderreport.Folder;
import ru.magnit.magreportbackend.dto.response.folderreport.FolderResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FolderResponseMapper implements Mapper<FolderResponse, Folder> {

    private final FolderReportResponseMapper reportMapper;

    @Override
    public FolderResponse from(Folder source) {
        var folderResponse = mapBaseProperties(source);
        folderResponse.setReports(reportMapper.from(source.getFolderReports()));
        folderResponse.setChildFolders(shallowMap(source.getChildFolders()));

        return folderResponse;
    }

    private FolderResponse mapBaseProperties(Folder source) {

        return new FolderResponse()
                .setId(source.getId())
                .setParentId(source.getParentFolder() == null ? null : source.getParentFolder().getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setCreated(source.getCreatedDateTime())
                .setModified(source.getModifiedDateTime());
    }

    @Override
    public FolderResponse shallowMap(Folder source) {

        return mapBaseProperties(source);
    }
}
