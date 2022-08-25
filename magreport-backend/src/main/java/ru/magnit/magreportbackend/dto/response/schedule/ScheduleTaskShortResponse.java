package ru.magnit.magreportbackend.dto.response.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTaskStatusEnum;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTaskTypeEnum;
import ru.magnit.magreportbackend.dto.response.report.ScheduleReportResponse;
import ru.magnit.magreportbackend.dto.response.user.UserShortResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ScheduleTaskShortResponse {

    private Long id;
    private String name;
    private String description;
    private ScheduleTaskStatusEnum status;
    private LocalDate expirationDate;
    private String code;
    private String reportBodyMail;
    private String reportTitleMail;
    private String errorBodyMail;
    private String errorTitleMail;
    private ScheduleReportResponse report;
    private ScheduleTaskTypeEnum typeTask;
    private UserShortResponse user;
    private Long renewalPeriod;
    private Boolean sendEmptyReport;
    private Long setMaxFailedStarts;
    private LocalDateTime created;
    private LocalDateTime modified;
    private List<ScheduleShortResponse> schedules = Collections.emptyList();
    private List<DestinationEmailResponse> destinationEmails = Collections.emptyList();
    private List<DestinationUserResponse> destinationUsers = Collections.emptyList();
    private List<DestinationRoleResponse> destinationRoles = Collections.emptyList();

}
