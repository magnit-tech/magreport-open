package ru.magnit.magreportbackend.dto.response.securityfilter;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
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
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SecurityFilterFolderResponse {
    private Long id;
    private Long parentId;
    private String name;
    private String description;
    private List<SecurityFilterFolderResponse> childFolders = Collections.emptyList();
    private List<SecurityFilterResponse> securityFilters = Collections.emptyList();
    private FolderAuthorityEnum authority;

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    private LocalDateTime created;

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    private LocalDateTime modified;

}
