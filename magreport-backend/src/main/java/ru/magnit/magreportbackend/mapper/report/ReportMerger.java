package ru.magnit.magreportbackend.mapper.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.report.ReportField;
import ru.magnit.magreportbackend.domain.report.ReportFolder;
import ru.magnit.magreportbackend.dto.request.report.ReportEditRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportFieldEditRequest;
import ru.magnit.magreportbackend.mapper.Merger;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportMerger implements Merger<Report, ReportEditRequest> {

    private final ReportFieldMerger fieldMerger;
    private final ReportFieldMapper fieldMapper;

    @Override
    public Report merge(Report target, ReportEditRequest source) {
        target
            .setFolder(new ReportFolder(source.getFolderId()))
            .setName(source.getName())
            .setDescription(source.getDescription())
            .setRequirementsLink(source.getRequirementsLink());

        updateExistingFields(target, source, fieldMerger);
        addNewFields(target, source, fieldMapper);

        return target;
    }

    private void addNewFields(Report target, ReportEditRequest source, ReportFieldMapper fieldMapper) {
        final var newFieldRequests = source.getFields().stream().filter(field -> field.getId() == null).collect(Collectors.toList());
        final var newFields = fieldMapper.from(newFieldRequests);
        newFields.forEach(field -> field.setReport(target));
        target.getFields().addAll(newFields);
    }

    private void updateExistingFields(Report target, ReportEditRequest source, ReportFieldMerger fieldMerger) {
        target.getFields().forEach(field -> fieldMerger.merge(field, getSourceField(source, field)));
    }

    private ReportFieldEditRequest getSourceField(ReportEditRequest source, ReportField field) {
        return Objects.requireNonNull(source.getFields().stream().filter(sf -> sf.getId().equals(field.getId())).findFirst().orElse(null));
    }
}
