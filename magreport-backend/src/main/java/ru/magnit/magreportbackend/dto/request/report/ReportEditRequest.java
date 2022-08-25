package ru.magnit.magreportbackend.dto.request.report;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.dto.request.filterreport.FilterGroupAddRequest;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class ReportEditRequest {

    @Schema(description ="ID отчета")
    private Long id;

    @Schema(description ="Наименование отчета")
    private String name;

    @Schema(description ="Описание отчета")
    private String description;

    @Schema(description ="Ссылка на реестр требований к отчету")
    private String requirementsLink;

    @Schema(description ="ID каталога")
    private Long folderId;

    @Schema(description ="Поля отчета")
    private List<ReportFieldEditRequest> fields = Collections.emptyList();

    @Schema(description ="Фильтры отчета")
    private FilterGroupAddRequest filterGroup;
}
