package ru.magnit.magreportbackend.dto.request.report;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class ReportFieldEditRequest {

    @Schema(description ="ID поля отчета")
    private Long id;

    @Schema(description ="ID поля набора данных")
    private Long dataSetFieldId;

    @Schema(description ="Наименования поля")
    private String name;

    @Schema(description ="Описание поля")
    private String description;

    @Schema(description ="Порядкоый номер поля")
    private Integer ordinal;

    @Schema(description ="Видимость поля в отчете")
    private Boolean visible;

    @Schema(description ="Тип поля для сводной таблицы")
    private Long pivotTypeId;
}
