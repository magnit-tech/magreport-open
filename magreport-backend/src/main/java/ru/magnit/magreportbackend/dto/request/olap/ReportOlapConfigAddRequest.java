package ru.magnit.magreportbackend.dto.request.olap;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
public class ReportOlapConfigAddRequest {

    private Long reportOlapConfigId;
    private Long jobId;
    private Long reportId;
    private Long userId;
    private Boolean isDefault;
    private Boolean isShare;
    private Boolean isCurrent;
    private OlapConfigUpdateRequest olapConfig;
}
