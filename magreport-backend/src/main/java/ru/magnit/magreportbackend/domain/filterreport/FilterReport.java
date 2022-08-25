package ru.magnit.magreportbackend.domain.filterreport;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstance;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobFilter;
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

@Entity(name = "FILTER_REPORT")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "FILTER_REPORT_ID"))
public class FilterReport extends EntityWithName {

    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILTER_REPORT_GROUP_ID")
    private FilterReportGroup group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILTER_INSTANCE_ID")
    private FilterInstance filterInstance;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "filterReport")
    private List<FilterReportField> fields = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "filterReport")
    private List<ReportJobFilter> reportJobFilters = Collections.emptyList();

    @Column(name = "ORDINAL")
    private Long ordinal;

    @Column(name = "IS_HIDDEN")
    private boolean hidden;

    @Column(name = "IS_MANDATORY")
    private boolean mandatory;

    @Column(name = "IS_ROOT_SELECTABLE")
    private boolean rootSelectable;

    @Column(name = "CODE")
    private String code;

    public FilterReport(Long id) {
        this.id = id;
    }

    @Override
    public FilterReport setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public FilterReport setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public FilterReport setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public FilterReport setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public FilterReport setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
