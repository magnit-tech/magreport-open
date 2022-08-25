package ru.magnit.magreportbackend.mapper.olap;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.olap.ReportOlapConfiguration;
import ru.magnit.magreportbackend.dto.response.olap.ReportOlapConfigResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.auth.UserShortResponseMapper;
import ru.magnit.magreportbackend.mapper.report.ReportShortResponseMapper;

@Service
@RequiredArgsConstructor
public class ReportOlapConfigurationResponseMapper implements Mapper <ReportOlapConfigResponse, ReportOlapConfiguration> {

    private final ReportShortResponseMapper reportShortResponseMapper;
    private final UserShortResponseMapper userShortResponseMapper;
    private final OlapConfigurationResponseMapper olapConfigurationResponseMapper;

    @Override
    public ReportOlapConfigResponse from(ReportOlapConfiguration source) {
        return new ReportOlapConfigResponse()
                .setReportOlapConfigId(source.getId())
                .setJobId(source.getReportJob() == null ? null : source.getReportJob().getId())
                .setReport(source.getReport() == null ? null: reportShortResponseMapper.from(source.getReport()))
                .setUser(source.getUser() == null ? null : userShortResponseMapper.from(source.getUser()))
                .setIsDefault(source.getIsDefault())
                .setIsShare(source.getIsShared())
                .setIsCurrent(source.getIsCurrent())
                .setCreator(userShortResponseMapper.from(source.getCreator()))
                .setOlapConfig(olapConfigurationResponseMapper.from(source.getOlapConfiguration()))
                .setCreated(source.getCreatedDateTime())
                .setModified(source.getModifiedDateTime());
    }
}
