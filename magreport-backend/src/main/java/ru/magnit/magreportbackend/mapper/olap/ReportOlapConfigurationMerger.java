package ru.magnit.magreportbackend.mapper.olap;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.olap.ReportOlapConfiguration;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.reportjob.ReportJob;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.request.olap.ReportOlapConfigAddRequest;
import ru.magnit.magreportbackend.mapper.Merger;
@Service
@RequiredArgsConstructor
public class ReportOlapConfigurationMerger implements Merger<ReportOlapConfiguration, ReportOlapConfigAddRequest> {

    private final OlapConfigurationMerger olapConfigurationMerger;

    @Override
    public ReportOlapConfiguration merge(ReportOlapConfiguration target, ReportOlapConfigAddRequest source) {

        return target
                .setReport(source.getReportId() == null ? null : new Report(source.getReportId()))
                .setReportJob(source.getJobId() == null? null : new ReportJob(source.getJobId()))
                .setUser(source.getUserId() == null ? null: new User(source.getUserId()))
                .setIsCurrent(source.getIsCurrent())
                .setIsDefault(source.getIsDefault())
                .setIsShared(source.getIsShare())
                .setOlapConfiguration(olapConfigurationMerger.merge(target.getOlapConfiguration(),source.getOlapConfig()));
    }
}
