package ru.magnit.magreportbackend.dto.request.filterinstance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Параметр для запрос вараинтов значений из фильтра типа TOKEN_INPUT")
public class ListValuesRequest {

    @Schema(description ="ID фильтра")
    private Long filterId;

    @Schema(description ="ID поля фильтра")
    private Long filterFieldId;

    @Schema(description ="Осуществлять поиск в регистронезависимом режиме")
    private Boolean isCaseInsensitive;

    @Schema(description ="Тип поиска")
    private LikenessType likenessType;

    @Schema(description ="Значение для поиска по LIKE")
    private String searchValue;

    @Schema(description ="Максимальное количество возвращаемых вариантов значений")
    private Long maxCount;
}
