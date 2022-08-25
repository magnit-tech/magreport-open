package ru.magnit.magreportbackend.dto.request.filterinstance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
@Schema(description = "Парамеры создания экземпляра фильтра")
public class FilterInstanceAddRequest {

    @Schema(description = "Id фильтра (заполняется только при редактировании фильтра)")
    private Long id;

    @Schema(description ="Id каталога в котором будет создан фильтр")
    private Long folderId;

    @Schema(description ="Id шаблона фильтра")
    private Long templateId;

    @Schema(description ="Id набора данных справочника")
    private Long dataSetId;

    @Schema(description ="Наименование фильтра")
    private String name;

    @Schema(description ="Код фильтра")
    private String code;

    @Schema(description ="Описание фильтра")
    private String description;

    @Schema(description ="Параметры полей создаваемого фильтра")
    private List<FilterInstanceFieldAddRequest> fields = Collections.emptyList();
}
