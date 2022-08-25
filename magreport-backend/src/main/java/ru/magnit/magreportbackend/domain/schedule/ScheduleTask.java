package ru.magnit.magreportbackend.domain.schedule;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplate;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobFilter;
import ru.magnit.magreportbackend.domain.user.User;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.FetchType.LAZY;


@Entity(name = "SCHEDULE_TASK")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "SCHEDULE_TASK_ID"))
public class ScheduleTask extends EntityWithName {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "EXPIRATION_DATE")
    private LocalDate expirationDate;

    @Column(name = "CODE")
    private String code;

    @Lob
    @Column(name = "REPORT_BODY_MAIL")
    private String reportBodyMail;

    @Column(name = "REPORT_TITLE_MAIL")
    private String reportTitleMail;

    @Lob
    @Column(name = "ERROR_BODY_MAIL")
    private String errorBodyMail;

    @Column(name = "ERROR_TITLE_MAIL")
    private String errorTitleMail;

    @Column(name = "EXPIRATION_CODE")
    private UUID expirationCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHEDULE_TASK_STATUS_ID")
    private ScheduleTaskStatus status;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "EXCEL_TEMPLATE_ID")
    private ExcelTemplate excelTemplate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "REPORT_ID")
    private Report report;

    @OneToMany(cascade = ALL, mappedBy = "scheduleTask")
    private List<ReportJobFilter> reportJobFilters = Collections.emptyList();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "SCHEDULE_TASK_TYPE_ID")
    private ScheduleTaskType taskType;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToMany(cascade = {MERGE})
    @JoinTable(
            name = "SCHEDULE_SCHEDULE_TASK",
            joinColumns = {@JoinColumn(name = "SCHEDULE_TASK_ID")},
            inverseJoinColumns = {@JoinColumn(name = "SCHEDULE_ID")}
    )
    private List<Schedule> scheduleList = Collections.emptyList();

    @OneToMany(cascade = ALL, mappedBy = "scheduleTask")
    private List<DestinationEmail> emailDestinations = Collections.emptyList();

    @OneToMany(cascade = ALL, mappedBy = "scheduleTask")
    private List<DestinationUser> userDestinations = Collections.emptyList();

    @OneToMany(cascade = ALL, mappedBy = "scheduleTask")
    private List<DestinationRole> roleDestinations = Collections.emptyList();

    @Column(name = "RENEWAL_PERIOD")
    private Long renewalPeriod;

    @Column(name = "SEND_EMPTY_REPORT")
    private Boolean sendEmptyReport;

    @Column(name = "FAILED_STARTS")
    private Long failedStart;

    @Column(name = "MAX_FAILED_STARTS")
    private Long maxFailedStart;

    @Override
    public ScheduleTask setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ScheduleTask setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ScheduleTask setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public ScheduleTask setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public ScheduleTask setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }

    public ScheduleTask(Long id) {
        this.id = id;
    }

}






