package ru.magnit.magreportbackend.dto.response.report;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class ScheduleReportResponse {

    @Schema(description ="ID отчета")
    private Long id;

    @Schema(description ="ID набора данных")
    private Long dataSetId;

    @Schema(description ="Наименование отчета")
    private String name;

    @Schema(description ="Описание отчета")
    private String description;

    @Schema(description ="Описание отчета")
    private String requirementsLink;

    @Schema(description ="Признак валидности отчета")
    private Boolean isValid = true;

    @Schema(description ="Путь к отчету")
    private List<FolderNodeResponse> path = Collections.emptyList();
}
