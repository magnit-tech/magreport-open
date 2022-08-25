package ru.magnit.magreportbackend.dto.response.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@Tag(name = "Информация о типе ролей")
public class RoleTypeResponse {

    @Schema(description ="ID типа")
    private Long id;

    @Schema(description ="ID родительского типа")
    private Long parentId;

    @Schema(description ="Наименование типа")
    private String name;

    @Schema(description ="Описание типа")
    private String description;

    @Schema(description ="Дочерние типы")
    private List<RoleTypeResponse> childTypes = Collections.emptyList();

    @Schema(description ="Дочерние роли")
    private List<RoleResponse> roles = Collections.emptyList();

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    @Schema(description ="Дата и время создания типа")
    private LocalDateTime created;

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    @Schema(description ="Дата и время последней модификации типа")
    private LocalDateTime modified;
}
