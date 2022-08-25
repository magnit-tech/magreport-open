package ru.magnit.magreportbackend.dto.response.dataset;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.dto.response.asm.AsmSecurityResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceDependenciesResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterResponse;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;

import java.time.LocalDateTime;
import java.util.List;

import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DataSetDependenciesResponse {
    private Long id;
    private Long typeId;
    private UserResponse creator;
    private String schemaName;
    private String objectName;
    private String name;
    private String description;
    private Boolean valid;
    private List<ReportResponse> reports;
    private List<FilterInstanceDependenciesResponse> filterInstances;
    private List<SecurityFilterResponse> securityFilters;
    private List<AsmSecurityResponse> asmSecurities;

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    LocalDateTime created;

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    LocalDateTime modified;

    private List<FolderNodeResponse> path;
}
