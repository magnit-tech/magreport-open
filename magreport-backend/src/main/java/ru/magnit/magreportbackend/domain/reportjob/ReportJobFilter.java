package ru.magnit.magreportbackend.domain.reportjob;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.BaseEntity;
import ru.magnit.magreportbackend.domain.filterreport.FilterReport;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationType;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTask;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity(name = "REPORT_JOB_FILTER")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "REPORT_JOB_FILTER_ID"))
public class ReportJobFilter extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPORT_JOB_ID")
    private ReportJob reportJob;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILTER_REPORT_ID")
    private FilterReport filterReport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILTER_OPERATION_TYPE_ID")
    private FilterOperationType filterOperationType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reportJobFilter")
    private List<ReportJobTuple> tuples = Collections.emptyList();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHEDULE_TASK_ID")
    private ScheduleTask scheduleTask;

    public ReportJobFilter(Long id) {
        this.id = id;
    }

    @Override
    public ReportJobFilter setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ReportJobFilter setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public ReportJobFilter setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
