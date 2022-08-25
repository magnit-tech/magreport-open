package ru.magnit.magreportbackend.dto.response.securityfilter;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;

import java.time.LocalDateTime;
import java.util.List;

import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

public record SecurityFilterShortResponse(
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    Long id,

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    String name,

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    String description,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    String userName,

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    LocalDateTime created,

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    LocalDateTime modified,

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    List<FolderNodeResponse> path
    ) {
}
