package ru.magnit.magreportbackend.mapper.folderreport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.folderreport.FolderReport;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.dto.response.report.ReportResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.auth.UserResponseMapper;

@Service
@RequiredArgsConstructor
public class FolderReportResponseMapper implements Mapper<ReportResponse, FolderReport> {

    private final UserResponseMapper userResponseMapper;

    @Override
    public ReportResponse from(FolderReport source) {
        var reportResponse = mapBaseProperties(source.getReport());
        reportResponse.setUserPublisher(userResponseMapper.from(source.getUser()));
        return reportResponse;
    }

    private ReportResponse mapBaseProperties(Report source) {
        return new ReportResponse()
                .setId(source.getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setDataSetId(source.getDataSet().getId())
                .setCreated(source.getCreatedDateTime())
                .setModified(source.getModifiedDateTime())
                .setRequirementsLink(source.getRequirementsLink())
                .setIsValid(source.getFields().stream().allMatch(reportField -> reportField.getDataSetField().getIsSync()));
    }
}
