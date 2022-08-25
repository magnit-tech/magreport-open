package ru.magnit.magreportbackend.dto.response.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ReportExcelTemplateResponse;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterGroupResponse;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class ReportResponse {

    @Schema(description = "ID отчета")
    private Long id;

    @Schema(description = "ID набора данных")
    private Long dataSetId;

    @Schema(description = "Наименование отчета")
    private String name;

    @Schema(description ="Описание отчета")
    private String description;

    @Schema(description ="Описание отчета")
    private String requirementsLink;

    @Schema(description ="Поля отчета")
    private List<ReportFieldResponse> fields = Collections.emptyList();

    @Schema(description ="Пользователь, опубликовавший отчет")
    private UserResponse userPublisher;

    @Schema(description ="Фильтры отчета")
    private FilterGroupResponse filterGroup;

    @Schema(description ="Параметры предыдущего запуска")
    private List<ReportJobFilterResponse> lastParameters;

    @Schema(description ="Признак валидности отчета")
    private Boolean isValid;

    @Schema(description ="Признак нахождения отчета в избранном")
    private boolean isFavorite = false;

    @Schema(description ="Путь к отчету")
    private List<FolderNodeResponse> path = Collections.emptyList();

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    @Schema(description ="Эксель шаблоны отчета")
    private List<ReportExcelTemplateResponse> excelTemplates;

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    @Schema(description ="Дата и время создания отчета")
    private LocalDateTime created;

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    @Schema(description ="Дата и время последнего изменения отчета")
    private LocalDateTime modified;

    @JsonIgnore
    public List<FilterReportResponse> getAllFilters() {

        final var result = new LinkedList<FilterReportResponse>();

        return unWindGroups(filterGroup, result);
    }

    private List<FilterReportResponse> unWindGroups(FilterGroupResponse group, List<FilterReportResponse> filters) {

        if (group.childGroups() != null) {
            group.childGroups().forEach(grp -> unWindGroups(grp, filters));
        }
        if (group.filters() != null) {
            filters.addAll(group.filters());
        }

        return filters;
    }
}
