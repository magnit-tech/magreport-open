package ru.magnit.magreportbackend.mapper.folderreport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.folderreport.Folder;
import ru.magnit.magreportbackend.domain.folderreport.FolderReport;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.dto.request.folderreport.FolderAddReportRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FolderReportMapper implements Mapper<FolderReport, FolderAddReportRequest> {

    @Override
    public FolderReport from(FolderAddReportRequest source) {

        return mapBaseProperties(source);
    }

    private FolderReport mapBaseProperties(FolderAddReportRequest source) {

        return new FolderReport()
                .setFolder(new Folder(source.getFolderId()))
                .setReport(new Report(source.getReportId()));
    }
}
