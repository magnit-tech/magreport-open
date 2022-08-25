package ru.magnit.magreportbackend.dto.response.olap;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.dto.response.report.ReportShortResponse;
import ru.magnit.magreportbackend.dto.response.user.UserShortResponse;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
public class ReportOlapConfigResponse {

    private Long reportOlapConfigId;
    private Long jobId;
    private Boolean isDefault;
    private Boolean isShare;
    private Boolean isCurrent;
    private ReportShortResponse report;
    private UserShortResponse user;
    private OlapConfigResponse olapConfig;
    private UserShortResponse creator;
    private LocalDateTime created;
    private LocalDateTime modified;
}
