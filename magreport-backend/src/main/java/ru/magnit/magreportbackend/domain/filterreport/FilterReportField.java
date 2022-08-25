package ru.magnit.magreportbackend.domain.filterreport;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;
import ru.magnit.magreportbackend.domain.report.ReportField;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobTupleField;

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

@Entity(name = "FILTER_REPORT_FIELD")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "FILTER_REPORT_FIELD_ID"))
public class FilterReportField extends EntityWithName {

    private static final long serialVersionUID = 1L;

    @Column(name = "ORDINAL")
    private Long ordinal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILTER_REPORT_ID")
    private FilterReport filterReport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILTER_INSTANCE_FIELD_ID")
    private FilterInstanceField filterInstanceField;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPORT_FIELD_ID")
    private ReportField reportField;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "filterReportField")
    private List<ReportJobTupleField> reportJobTupleFields = Collections.emptyList();

    @Column(name = "EXPAND")
    private boolean expand;

    public FilterReportField(Long id) {
        this.id = id;
    }

    @Override
    public FilterReportField setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public FilterReportField setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public FilterReportField setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public FilterReportField setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public FilterReportField setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
