package ru.magnit.magreportbackend.dto.response.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResultResponse;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PublishedReportResponse {


    private Long id;
    private Long dataSetId;
    private String name;
    private String description;
    private String requirementsLink;
    private UserResponse userPublisher;
    private List<FolderNodeResponse> path = Collections.emptyList();
    private List<FolderSearchResultResponse> folders = Collections.emptyList();

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    private LocalDateTime created;

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    private LocalDateTime modified;
}
