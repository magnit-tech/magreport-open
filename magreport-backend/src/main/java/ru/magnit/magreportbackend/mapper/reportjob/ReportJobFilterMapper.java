package ru.magnit.magreportbackend.mapper.reportjob;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterreport.FilterReport;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationType;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobFilter;
import ru.magnit.magreportbackend.dto.request.reportjob.ReportJobFilterRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ReportJobFilterMapper implements Mapper<ReportJobFilter, ReportJobFilterRequest> {

    private final ReportJobTupleMapper tupleMapper;

    @Override
    public ReportJobFilter from(ReportJobFilterRequest source) {
        final var result = new ReportJobFilter()
            .setFilterReport(new FilterReport(source.getFilterId()))
            .setFilterOperationType(new FilterOperationType(source.getOperationType()))
            .setTuples(tupleMapper.from(source.getParameters()));
        result.getTuples().forEach(tuple -> tuple.setReportJobFilter(result));

        return result;
    }
}
