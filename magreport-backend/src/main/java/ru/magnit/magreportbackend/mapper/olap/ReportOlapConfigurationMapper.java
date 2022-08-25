package ru.magnit.magreportbackend.mapper.olap;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.olap.ReportOlapConfiguration;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.reportjob.ReportJob;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.request.olap.ReportOlapConfigAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ReportOlapConfigurationMapper implements Mapper<ReportOlapConfiguration, ReportOlapConfigAddRequest> {

    @Override
    public ReportOlapConfiguration from(ReportOlapConfigAddRequest source) {
    return new ReportOlapConfiguration()
                .setReportJob(source.getJobId() == null ? null : new ReportJob(source.getJobId()))
                .setUser(source.getUserId() == null ? null : new User(source.getUserId()))
                .setReport(source.getReportId() == null ? null : new Report(source.getReportId()))
                .setIsDefault(source.getIsDefault())
                .setIsShared(source.getIsShare())
                .setIsCurrent(source.getIsCurrent());

    }
}
