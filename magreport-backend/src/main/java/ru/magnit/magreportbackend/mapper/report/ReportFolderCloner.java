package ru.magnit.magreportbackend.mapper.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.report.ReportFolder;
import ru.magnit.magreportbackend.mapper.Cloner;

@Service
@RequiredArgsConstructor
public class ReportFolderCloner implements Cloner<ReportFolder> {

    private final ReportCloner reportCloner;
    private final ReportFolderRoleCloner reportFolderRoleCloner;

    @Override
    public ReportFolder clone(ReportFolder source) {
        var folder = new ReportFolder()
                .setName(source.getName())
                .setDescription(source.getDescription());

        final var reports = reportCloner.clone(source.getReports());
        reports.forEach(report -> report.setFolder(folder));
        folder.setReports(reports);

        final var folderRoles = reportFolderRoleCloner.clone(source.getFolderRoles());
        folderRoles.forEach(f -> f.setFolder(folder));
        folder.setFolderRoles(folderRoles);

        return folder;
    }
}
