package ru.magnit.magreportbackend.domain.reportjobstats;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.BaseEntity;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.reportjob.ReportJob;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobFilter;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobState;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobStatus;
import ru.magnit.magreportbackend.domain.user.User;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity(name = "REPORT_JOB_STATISTICS")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "REPORT_JOB_STATISTICS_ID"))
public class ReportJobStatistics extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPORT_JOB_ID")
    private ReportJob reportJob;

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

    @Column(name = "ROW_COUNT")
    private Long rowCount;

    public ReportJobStatistics(Long id) {
        this.id = id;
    }

    @Override
    public ReportJobStatistics setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ReportJobStatistics setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public ReportJobStatistics setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
