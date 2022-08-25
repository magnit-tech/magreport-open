package ru.magnit.magreportbackend.mapper.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.report.ReportFolder;
import ru.magnit.magreportbackend.dto.response.report.ReportFolderResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ReportFolderResponseMapper implements Mapper<ReportFolderResponse, ReportFolder> {

    private final ReportResponseMapper reportMapper;

    @Override
    public ReportFolderResponse from(ReportFolder source) {
        var folderResponse = mapBaseProperties(source);
        folderResponse.setReports(reportMapper.from(source.getReports()));
        folderResponse.setChildFolders(shallowMap(source.getChildFolders()));
        return folderResponse;
    }

    private ReportFolderResponse mapBaseProperties(ReportFolder source) {
        return new ReportFolderResponse()
            .setId(source.getId())
            .setParentId(source.getParentFolder() == null ? null : source.getParentFolder().getId())
            .setName(source.getName())
            .setDescription(source.getDescription())
            .setCreated(source.getCreatedDateTime())
            .setModified(source.getModifiedDateTime());
    }

    @Override
    public ReportFolderResponse shallowMap(ReportFolder source) {

        return mapBaseProperties(source);
    }
}
