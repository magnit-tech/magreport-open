package ru.magnit.magreportbackend.dto.request.filterinstance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.dto.tuple.TupleValue;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "Параметр для запроса дочерних узлов из фильтра типа HIERARCHY")
public class ChildNodesRequest {

    @Schema(description = "ID фильтра")
    private Long filterId;

    @Schema(description = "Путь к корню запрашиваемой ветки")
    private List<TupleValue> pathNodes = Collections.emptyList();
}
