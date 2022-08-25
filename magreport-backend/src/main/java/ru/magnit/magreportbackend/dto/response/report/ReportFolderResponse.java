package ru.magnit.magreportbackend.dto.response.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
public class ReportFolderResponse {

    private Long id;
    private Long parentId;
    private String name;
    private String description;
    private List<ReportFolderResponse> childFolders = Collections.emptyList();
    private List<ReportResponse> reports = Collections.emptyList();
    private FolderAuthorityEnum authority;

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    private LocalDateTime created;

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    private LocalDateTime modified;
}
