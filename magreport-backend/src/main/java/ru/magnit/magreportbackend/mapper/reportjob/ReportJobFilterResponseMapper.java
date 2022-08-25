package ru.magnit.magreportbackend.mapper.reportjob;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobFilter;
import ru.magnit.magreportbackend.dto.response.report.ReportJobFilterResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ReportJobFilterResponseMapper implements Mapper<ReportJobFilterResponse, ReportJobFilter> {

    private final TupleMapper tupleMapper;

    @Override
    public ReportJobFilterResponse from(ReportJobFilter source) {
        if (source == null) return null;
        return new ReportJobFilterResponse()
            .setId(source.getId())
            .setFilterId(source.getFilterReport().getId())
            .setFilterType(FilterTypeEnum.getByOrdinal(source.getFilterReport().getFilterInstance().getFilterTemplate().getType().getId()))
            .setOperationType(FilterOperationTypeEnum.getById(source.getFilterOperationType().getId()))
            .setParameters(tupleMapper.from(source.getTuples()));
    }
}
