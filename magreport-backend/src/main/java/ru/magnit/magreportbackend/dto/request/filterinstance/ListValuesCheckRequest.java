package ru.magnit.magreportbackend.dto.request.filterinstance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.magnit.magreportbackend.dto.tuple.TupleValue;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Параметр для проверки вариантов значений из фильтра типа VALUE_LIST")
public class ListValuesCheckRequest {

    @Schema(description = "ID фильтра")
    private Long filterId;

    @Schema(description = "Проверяемые значения")
    private List<TupleValue> values = Collections.emptyList();
}
