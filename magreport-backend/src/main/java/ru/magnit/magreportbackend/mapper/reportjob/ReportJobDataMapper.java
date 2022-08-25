package ru.magnit.magreportbackend.mapper.reportjob;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.reportjob.ReportJob;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobStatusEnum;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobData;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceViewMapper;

import java.util.LinkedList;

@Service
@RequiredArgsConstructor
public class ReportJobDataMapper implements Mapper<ReportJobData, ReportJob> {

    private final DataSourceViewMapper dataSourceViewMapper;
    private final ReportDataMapper reportDataMapper;
    private final ReportJobFilterDataMapper parametersMapper;

    @Override
    public ReportJobData from(ReportJob source) {
        return new ReportJobData(
                source.getId(),
                source.getReport().getId(),
                source.getReport().getDataSet().getId(),
                source.getReport().getDataSet().getType().getId(),
                source.getUser().getId(),
                source.getUser().getName(),
                source.getStatus().getId(),
                source.getState().getId(),
                source.getRowCount(),
                ReportJobStatusEnum.getById(source.getStatus().getId()) == ReportJobStatusEnum.COMPLETE,
                dataSourceViewMapper.from(source.getReport().getDataSet().getDataSource()),
                reportDataMapper.from(source.getReport()),
                parametersMapper.from(source.getReportJobFilters()),
                new LinkedList<>());
    }
}
