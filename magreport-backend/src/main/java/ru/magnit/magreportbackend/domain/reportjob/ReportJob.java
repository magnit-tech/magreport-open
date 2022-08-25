package ru.magnit.magreportbackend.domain.reportjob;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.BaseEntity;
import ru.magnit.magreportbackend.domain.olap.ReportOlapConfiguration;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.user.User;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity(name = "REPORT_JOB")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "REPORT_JOB_ID"))
public class ReportJob extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Lob
    @Column(name = "SQL_QUERY")
    private String sqlQuery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPORT_ID")
    private Report report;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPORT_JOB_STATUS_ID")
    private ReportJobStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPORT_JOB_STATE_ID")
    private ReportJobState state;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reportJob")
    private List<ReportJobFilter> reportJobFilters = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reportJob")
    private List<ReportOlapConfiguration> reportOlapConfigurations = Collections.emptyList();

    @Column(name = "ROW_COUNT")
    private Long rowCount;

    @Column(name = "MESSAGE")
    private String message;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reportJob")
    private List<ReportJobUser> reportJobUsers = Collections.emptyList();
/*
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "REPORT_JOB_USER",
            joinColumns = {@JoinColumn(name = "REPORT_JOB_ID")},
            inverseJoinColumns = {@JoinColumn(name = "USER_ID")}
    )

    private List<User> userList = Collections.emptyList();


 */
    public ReportJob(Long id) {
        this.id = id;
    }

    @Override
    public ReportJob setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ReportJob setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public ReportJob setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
