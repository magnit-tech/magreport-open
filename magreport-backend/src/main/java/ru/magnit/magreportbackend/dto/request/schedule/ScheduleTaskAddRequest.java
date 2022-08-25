package ru.magnit.magreportbackend.dto.request.schedule;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.dto.request.reportjob.ReportJobFilterRequest;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor

public class ScheduleTaskAddRequest {

    private Long id;
    private LocalDate expirationDate;
    private String code;
    private String reportBodyMail;
    private String reportTitleMail;
    private String errorBodyMail;
    private String errorTitleMail;
    private Long excelTemplateId;
    private Long reportId;
    private Long taskTypeId;
    private String name;
    private String description;
    private Long renewalPeriod;
    private Boolean sendEmptyReport;
    private Long maxFailedStarts;
    private List<DestinationEmailAddRequest> destinationEmails = Collections.emptyList();
    private List<DestinationUserAddRequest> destinationUsers = Collections.emptyList();
    private List<DestinationRoleAddRequest> destinationRoles = Collections.emptyList();
    private List<ScheduleRequest> schedules = Collections.emptyList();
    private List<ReportJobFilterRequest> reportJobFilter = Collections.emptyList();

}
