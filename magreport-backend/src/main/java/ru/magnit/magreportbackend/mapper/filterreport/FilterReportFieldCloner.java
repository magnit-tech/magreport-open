package ru.magnit.magreportbackend.mapper.filterreport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportField;
import ru.magnit.magreportbackend.domain.report.ReportField;
import ru.magnit.magreportbackend.mapper.Cloner;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class FilterReportFieldCloner implements Cloner<FilterReportField> {
    @Override
    public FilterReportField clone(FilterReportField source) {
        return new FilterReportField()
            .setName(source.getName())
            .setDescription(source.getDescription())
            .setOrdinal(source.getOrdinal())
            .setExpand(source.isExpand())
            .setFilterInstanceField(source.getFilterInstanceField());
    }

    @SuppressWarnings("All")
    @Override
    public FilterReportField clone(FilterReportField source, Object context) {
        final var fieldCache = (Map<Integer, ReportField>) context;
        final var reportField = source.getReportField() == null ? null : fieldCache.get(source.getReportField().getOrdinal());

        return new FilterReportField()
            .setName(source.getName())
            .setDescription(source.getDescription())
            .setOrdinal(source.getOrdinal())
            .setExpand(source.isExpand())
            .setFilterInstanceField(source.getFilterInstanceField())
            .setReportField(reportField);
    }
}
