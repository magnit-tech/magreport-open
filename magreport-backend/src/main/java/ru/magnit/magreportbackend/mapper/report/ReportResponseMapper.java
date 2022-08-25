package ru.magnit.magreportbackend.mapper.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportGroup;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.dto.response.report.ReportFieldResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.auth.UserResponseMapper;
import ru.magnit.magreportbackend.mapper.filterreport.FilterGroupResponseMapper;

@Service
@RequiredArgsConstructor
public class ReportResponseMapper implements Mapper<ReportResponse, Report> {

    private final ReportFieldResponseMapper fieldResponseMapper;
    private final FilterGroupResponseMapper filterGroupResponseMapper;
    private final UserResponseMapper userResponseMapper;

    @Override
    public ReportResponse from(Report source) {
        return mapBaseProperties(source);
    }

    private ReportResponse mapBaseProperties(Report source) {
        final var reportResponse = new ReportResponse()
            .setId(source.getId())
            .setName(source.getName())
            .setDescription(source.getDescription())
            .setDataSetId(source.getDataSet().getId())
            .setRequirementsLink(source.getRequirementsLink())
            .setUserPublisher(userResponseMapper.from(source.getUser()))
            .setCreated(source.getCreatedDateTime())
            .setModified(source.getModifiedDateTime())
            .setFields(fieldResponseMapper.from(source.getFields()))
            .setFilterGroup(filterGroupResponseMapper.from(getRootFilterGroup(source)));
        reportResponse.setIsValid(reportResponse.getFields().stream().allMatch(ReportFieldResponse::getValid));

        return reportResponse;
    }

    private FilterReportGroup getRootFilterGroup(Report source) {

        return source.getFilterReportGroups()
            .stream()
            .filter(group -> group.getParentGroup() == null)
            .findFirst()
            .orElse(null);
    }
}
