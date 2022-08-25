package ru.magnit.magreportbackend.mapper.filterreport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterreport.FilterReport;
import ru.magnit.magreportbackend.mapper.Cloner;


@Service
@RequiredArgsConstructor
public class FilterReportCloner implements Cloner<FilterReport> {

    private final FilterReportFieldCloner fieldCloner;

    @Override
    public FilterReport clone(FilterReport source, Object context) {
        final var filterReport = new FilterReport()
                .setFilterInstance(source.getFilterInstance())
                .setName(source.getName())
                .setCode(source.getCode())
                .setDescription(source.getDescription())
                .setHidden(source.isHidden())
                .setOrdinal(source.getOrdinal())
                .setMandatory(source.isMandatory())
                .setRootSelectable(source.isRootSelectable());

        final var fields = fieldCloner.clone(source.getFields(),context);
        fields.forEach(field -> field.setFilterReport(filterReport));
        filterReport.setFields(fields);

        return filterReport;
    }

    @Override
    public FilterReport clone(FilterReport source) {
      return clone(source,null);
    }
}
