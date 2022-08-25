package ru.magnit.magreportbackend.mapper.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.report.ReportFolder;
import ru.magnit.magreportbackend.dto.request.report.ReportAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ReportMapper implements Mapper<Report, ReportAddRequest> {

    @Override
    public Report from(ReportAddRequest source) {
        return mapBaseProperties(source);
    }

    private Report mapBaseProperties(ReportAddRequest source) {
        return new Report()
            .setFolder(new ReportFolder(source.getFolderId()))
            .setName(source.getName())
            .setDescription(source.getDescription())
            .setRequirementsLink(source.getRequirementsLink())
            .setDataSet(new DataSet(source.getDataSetId()));
    }
}
