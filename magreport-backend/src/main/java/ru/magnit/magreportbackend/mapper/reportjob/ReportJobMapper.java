package ru.magnit.magreportbackend.mapper.reportjob;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.reportjob.ReportJob;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobState;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobStateEnum;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobStatus;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobStatusEnum;
import ru.magnit.magreportbackend.dto.request.reportjob.ReportJobAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ReportJobMapper implements Mapper<ReportJob, ReportJobAddRequest> {

    private final ReportJobFilterMapper filterMapper;

    @Override
    public ReportJob from(ReportJobAddRequest source) {
        return mapBaseProperties(source);
    }

    private ReportJob mapBaseProperties(ReportJobAddRequest source) {
        final var reportJob = new ReportJob()
            .setReport(new Report(source.getReportId()))
            .setRowCount(0L)
            .setStatus(new ReportJobStatus(ReportJobStatusEnum.SCHEDULED.getId()))
            .setState(new ReportJobState(ReportJobStateEnum.NORMAL.getId()))
            .setReportJobFilters(filterMapper.from(source.getParameters()));
        reportJob.getReportJobFilters().forEach(filter -> filter.setReportJob(reportJob));

        return reportJob;
    }
}
