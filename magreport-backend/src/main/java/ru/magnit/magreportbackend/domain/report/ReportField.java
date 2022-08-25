package ru.magnit.magreportbackend.domain.report;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportField;

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

@Entity(name = "REPORT_FIELD")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "REPORT_FIELD_ID"))
public class ReportField extends EntityWithName {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "ORDINAL")
    private Integer ordinal;

    @Column(name = "VISIBLE")
    private Boolean visible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPORT_ID")
    private Report report;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DATASET_FIELD_ID")
    private DataSetField dataSetField;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PIVOT_FIELD_TYPE_ID")
    private PivotFieldType pivotFieldType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reportField")
    private List<FilterReportField> filterReportFields = Collections.emptyList();

    public ReportField(Long id) {
        this.id = id;
    }

    @Override
    public ReportField setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ReportField setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ReportField setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public ReportField setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public ReportField setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
