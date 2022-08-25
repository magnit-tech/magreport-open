package ru.magnit.magreportbackend.dto.response.filterinstance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportResponse;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTemplateResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterResponse;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class FilterInstanceDependenciesResponse {
    private Long id;
    private String name;
    private String description;
    private UserResponse creator;
    private FilterTypeEnum type;
    private List<FilterOperationTypeEnum> supportedOperations;
    private FilterTemplateResponse template;
    private List<FilterInstanceFieldResponse> fields;
    private List<FilterReportResponse> reportFilters;
    private Boolean valid;
    private List<FolderNodeResponse> path;
    private List<ReportResponse> reports;
    private List<SecurityFilterResponse> securityFilters;
}
