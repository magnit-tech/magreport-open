package ru.magnit.magreportbackend.dto.response.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTaskStatusEnum;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTaskTypeEnum;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ExcelTemplateResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportJobFilterResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportResponse;
import ru.magnit.magreportbackend.dto.response.user.UserShortResponse;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ScheduleTaskResponse {

    private Long id;
    private String name;
    private String description;
    private ScheduleTaskStatusEnum status;
    private LocalDate expirationDate;
    private UUID expirationCode;
    private String code;
    private String reportBodyMail;
    private String reportTitleMail;
    private String errorBodyMail;
    private String errorTitleMail;
    private ExcelTemplateResponse excelTemplate;
    private ReportResponse report;
    private ScheduleTaskTypeEnum typeTask;
    private UserShortResponse user;
    private Long renewalPeriod;
    private Boolean sendEmptyReport;
    private Long maxFailedStarts;
    private Long failedStart;
    private List<ScheduleShortResponse> schedules = Collections.emptyList();
    private List<DestinationEmailResponse> destinationEmails = Collections.emptyList();
    private List<DestinationUserResponse> destinationUsers = Collections.emptyList();
    private List<DestinationRoleResponse> destinationRoles = Collections.emptyList();
    private List<ReportJobFilterResponse> reportJobFilters = Collections.emptyList();


}
