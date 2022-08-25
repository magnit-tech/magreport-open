package ru.magnit.magreportbackend.mapper.reportjob;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportFieldData;
import ru.magnit.magreportbackend.mapper.Mapper;

import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportDataMapper implements Mapper<ReportData, Report> {

    private final ReportFieldDataMapper reportFieldMapper;
    private final ReportFilterGroupDataMapper filtersMapper;

    @Override
    public ReportData from(Report source) {

        return new ReportData(
                source.getId(),
                source.getName(),
                source.getDescription(),
                source.getDataSet().getSchemaName(),
                source.getDataSet().getObjectName(),
                reportFieldMapper.from(source.getFields()).stream().sorted(Comparator.comparingLong(ReportFieldData::ordinal)).collect(Collectors.toList()),
                filtersMapper.from(source.getFilterReportGroups().stream().filter(o -> o.getParentGroup() == null).findFirst().orElse(null)));
    }
}
