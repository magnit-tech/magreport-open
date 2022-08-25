package ru.magnit.magreportbackend.mapper.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.report.ReportFolder;
import ru.magnit.magreportbackend.dto.response.folderreport.FolderResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ReportFolderResponse2Mapper implements Mapper<FolderResponse, ReportFolder> {
    private final ReportResponseMapper reportMapper;

    @Override
    public FolderResponse from(ReportFolder source) {
        var folderResponse = mapBaseProperties(source);
        folderResponse.setReports(reportMapper.from(source.getReports()));
        folderResponse.setChildFolders(shallowMap(source.getChildFolders()));

        return folderResponse;
    }

    private FolderResponse mapBaseProperties(ReportFolder source) {

        return new FolderResponse()
            .setId(source.getId())
            .setParentId(source.getParentFolder() == null ? null : source.getParentFolder().getId())
            .setName(source.getName())
            .setDescription(source.getDescription())
            .setCreated(source.getCreatedDateTime())
            .setModified(source.getModifiedDateTime());
    }

    @Override
    public FolderResponse shallowMap(ReportFolder source) {

        return mapBaseProperties(source);
    }
}
