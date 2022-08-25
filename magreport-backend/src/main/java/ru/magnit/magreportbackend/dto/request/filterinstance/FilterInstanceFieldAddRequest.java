package ru.magnit.magreportbackend.dto.request.filterinstance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
@Schema(description = "Парамеры создания поля экземпляра фильтра")
public class FilterInstanceFieldAddRequest {

    @Schema(description ="Уровень поля")
    private Long level;

    @Schema(description ="Тип поля")
    private FilterFieldTypeEnum type;

    @Schema(description ="id поля шаблона фильтра", hidden = true)
    private Long templateFieldId;

    @Schema(description ="id поля набора данных справочника")
    private Long dataSetFieldId;

    @Schema(description ="Наименование поля")
    private String name;

    @Schema(description ="Описание поля")
    private String description;

    @Schema(description ="Подмена выбранного уровня дочерними элементами")
    private Boolean expand;
}
