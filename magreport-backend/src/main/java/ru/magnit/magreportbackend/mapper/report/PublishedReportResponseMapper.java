package ru.magnit.magreportbackend.mapper.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.dto.response.report.PublishedReportResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.auth.UserResponseMapper;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class PublishedReportResponseMapper implements Mapper<PublishedReportResponse, Report> {

    private final UserResponseMapper userResponseMapper;

    @Override
    public PublishedReportResponse from(Report source) {
        return new PublishedReportResponse()
                .setId(source.getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setDataSetId(source.getDataSet().getId())
                .setCreated(source.getCreatedDateTime())
                .setModified(source.getModifiedDateTime())
                .setRequirementsLink(source.getRequirementsLink())
                .setUserPublisher(userResponseMapper.from(source.getUser()))
                .setPath(Collections.emptyList())
                .setFolders(Collections.emptyList());

    }
}
