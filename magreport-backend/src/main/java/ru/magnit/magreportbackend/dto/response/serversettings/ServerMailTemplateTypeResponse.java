package ru.magnit.magreportbackend.dto.response.serversettings;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@Tag(name = "Информация о типе шаблонов писем")
public class ServerMailTemplateTypeResponse {

    @Schema(description ="ID типа")
    private Long id;

    @Schema(description ="ID родительского типа")
    private Long parentId;

    @Schema(description ="Наименование типа")
    private String name;

    @Schema(description ="Описание типа")
    private String description;

    @Schema(description ="Пользователь")
    private UserResponse user;

    @Schema(description ="Дочерние типы")
    private List<ServerMailTemplateTypeResponse> childTypes = Collections.emptyList();

    @Schema(description ="Дочерние шаблоны")
    private List<ServerMailTemplateResponse> systemMailTemplates = Collections.emptyList();

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    @Schema(description ="Дата и время создания типа")
    private LocalDateTime created;

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    @Schema(description ="Дата и время последней модификации типа")
    private LocalDateTime modified;

}
