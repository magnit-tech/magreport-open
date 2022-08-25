package ru.magnit.magreportbackend.mapper.reportjob;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportGroup;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportFilterGroupData;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ReportFilterGroupDataMapper implements Mapper<ReportFilterGroupData, FilterReportGroup> {

    private final ReportFilterDataMapper filterDataMapper;

    @Override
    public ReportFilterGroupData from(FilterReportGroup source) {
        if (source == null) return null;

        return new ReportFilterGroupData(
                source.getId(),
                source.getParentGroup() == null ? null : source.getParentGroup().getId(),
                source.getCode(),
                source.getParentGroup() == null ? null : source.getParentGroup().getCode(),
                source.getTypeEnum(),
                filterDataMapper.from(source.getFilterReports()),
                from(source.getChildGroups()));
    }
}
