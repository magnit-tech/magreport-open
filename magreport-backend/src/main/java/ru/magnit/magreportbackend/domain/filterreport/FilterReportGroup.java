package ru.magnit.magreportbackend.domain.filterreport;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;
import ru.magnit.magreportbackend.domain.report.Report;

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

@Entity(name = "FILTER_REPORT_GROUP")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "FILTER_REPORT_GROUP_ID"))
public class FilterReportGroup extends EntityWithName {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "ORDINAL")
    private Long ordinal;

    @Column(name = "LINKED_FILTERS")
    private Boolean linkedFilters;

    @Column(name = "CODE")
    private String code;

    @Column(name = "MANDATORY")
    private Boolean mandatory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPORT_FILTER_GROUP_OPERATION_TYPE_ID")
    private GroupOperationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPORT_ID")
    private Report report;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private FilterReportGroup parentGroup;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentGroup")
    private List<FilterReportGroup> childGroups = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
    private List<FilterReport> filterReports = Collections.emptyList();

    public GroupOperationTypeEnum getTypeEnum() {
        return GroupOperationTypeEnum.values()[type.getId().intValue()];
    }

    public FilterReportGroup(Long id) {
        this.id = id;
    }

    @Override
    public FilterReportGroup setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public FilterReportGroup setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public FilterReportGroup setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public FilterReportGroup setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public FilterReportGroup setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
