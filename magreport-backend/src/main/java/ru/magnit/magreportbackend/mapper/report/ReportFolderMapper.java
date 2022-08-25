package ru.magnit.magreportbackend.mapper.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.report.ReportFolder;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ReportFolderMapper implements Mapper<ReportFolder, FolderAddRequest> {

    @Override
    public ReportFolder from(FolderAddRequest source) {
        return mapBaseProperties(source);
    }

    private ReportFolder mapBaseProperties(FolderAddRequest source) {

        return new ReportFolder()
                .setParentFolder(source.getParentId() == null ? null : new ReportFolder(source.getParentId()))
                .setName(source.getName())
                .setDescription(source.getDescription());
    }
}
