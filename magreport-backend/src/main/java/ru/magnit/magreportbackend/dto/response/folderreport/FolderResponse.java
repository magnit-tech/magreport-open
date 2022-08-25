package ru.magnit.magreportbackend.dto.response.folderreport;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.dto.response.report.ReportResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
public class FolderResponse {

    @Schema(name = "ID каталога")
    private Long id;

    @Schema(name = "ID родительского каталога")
    private Long parentId;

    @Schema(name = "Наименование каталога")
    private String name;

    @Schema(name = "Описание каталога")
    private String description;

    @Schema(name = "Дочерние каталоги")
    private List<FolderResponse> childFolders = Collections.emptyList();

    @Schema(name = "Отчеты")
    private List<ReportResponse> reports = Collections.emptyList();

    @Schema(name = "Уровень доступа текущего пользователя")
    private FolderAuthorityEnum authority;

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    @Schema(name = "Дата и время создания каталога")
    private LocalDateTime created;

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    @Schema(name = "Дата и время последней модификации каталога")
    private LocalDateTime modified;
}
