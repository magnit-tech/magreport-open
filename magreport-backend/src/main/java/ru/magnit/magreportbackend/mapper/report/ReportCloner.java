package ru.magnit.magreportbackend.mapper.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportGroup;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.report.ReportField;
import ru.magnit.magreportbackend.mapper.Cloner;
import ru.magnit.magreportbackend.mapper.exceltemplate.ReportExcelTemplateCloner;
import ru.magnit.magreportbackend.mapper.filterreport.FilterReportGroupCloner;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportCloner implements Cloner<Report> {

    private final ReportFieldCloner fieldCloner;
    private final FilterReportGroupCloner filterReportGroupCloner;
    private final ReportExcelTemplateCloner reportExcelTemplateCloner;

    @Override
    public Report clone(Report source) {
        final var report = new Report()
            .setName(source.getName())
            .setDescription(source.getDescription())
            .setDataSet(source.getDataSet())
            .setRequirementsLink(source.getRequirementsLink());

        final var fields = fieldCloner.clone(source.getFields());
        fields.forEach(field -> field.setReport(report));
        report.setFields(fields);

        final var fieldCache = fields.stream().collect(Collectors.toMap(ReportField::getOrdinal, reportField -> reportField));

        final var filterReportGroups = filterReportGroupCloner.clone(source.getFilterReportGroups(), fieldCache);
        setReport(filterReportGroups, report);
        report.setFilterReportGroups(filterReportGroups);

        final var templates = reportExcelTemplateCloner.clone(source.getReportExcelTemplates());
        templates.forEach(template -> template.setReport(report));
        report.setReportExcelTemplates(templates);


        return report;
    }

    private void setReport(List<FilterReportGroup> filterReportGroups, Report report) {
        filterReportGroups.forEach(filterReportGroup -> {
            filterReportGroup.setReport(report);
            setReport(filterReportGroup.getChildGroups(), report);
        });
    }
}
