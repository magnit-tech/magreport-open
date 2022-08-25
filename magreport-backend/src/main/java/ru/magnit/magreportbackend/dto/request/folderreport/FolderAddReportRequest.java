package ru.magnit.magreportbackend.dto.request.folderreport;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
public class FolderAddReportRequest {

    private Long reportId;
    private Long folderId;
}
