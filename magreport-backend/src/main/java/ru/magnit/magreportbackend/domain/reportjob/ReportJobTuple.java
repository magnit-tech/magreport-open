package ru.magnit.magreportbackend.domain.reportjob;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.BaseEntity;

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

@Entity(name = "REPORT_JOB_TUPLE")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "REPORT_JOB_TUPLE_ID"))
public class ReportJobTuple extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPORT_JOB_FILTER_ID")
    private ReportJobFilter reportJobFilter;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reportJobTuple")
    private List<ReportJobTupleField> fields = Collections.emptyList();

    public ReportJobTuple(Long id) {
        this.id = id;
    }

    @Override
    public ReportJobTuple setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ReportJobTuple setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public ReportJobTuple setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
