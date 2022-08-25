package ru.magnit.magreportbackend.domain.reportjob;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.BaseEntity;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportField;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serial;
import java.time.LocalDateTime;

@Entity(name = "REPORT_JOB_TUPLE_FIELD")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "REPORT_JOB_TUPLE_FIELD_ID"))
public class ReportJobTupleField extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "VAL")
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPORT_JOB_TUPLE_ID")
    private ReportJobTuple reportJobTuple;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILTER_REPORT_FIELD_ID")
    private FilterReportField filterReportField;

    public ReportJobTupleField(Long id) {
        this.id = id;
    }

    @Override
    public ReportJobTupleField setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ReportJobTupleField setCreatedDateTime(LocalDateTime createdDateTime) {

        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public ReportJobTupleField setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
