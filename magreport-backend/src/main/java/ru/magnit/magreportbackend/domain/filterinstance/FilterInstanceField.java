package ru.magnit.magreportbackend.domain.filterinstance;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportField;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateField;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterDataSetField;

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
import java.util.LinkedList;
import java.util.List;

@Entity(name = "FILTER_INSTANCE_FIELD")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "FILTER_INSTANCE_FIELD_ID"))
public class FilterInstanceField extends EntityWithName {

    private static final long serialVersionUID = 1L;

    @Column(name = "LEVEL")
    private Long level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILTER_INSTANCE_ID")
    private FilterInstance instance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILTER_TEMPLATE_FIELD_ID")
    private FilterTemplateField templateField;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DATASET_FIELD_ID")
    private DataSetField dataSetField;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "filterInstanceField")
    private List<FilterReportField> filterReportFields = new LinkedList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "filterInstanceField")
    private List<SecurityFilterDataSetField> fieldMappings = Collections.emptyList();

    @Column(name = "EXPAND")
    private Boolean expand;

    public FilterInstanceField(Long id) {
        this.id = id;
    }

    @Override
    public FilterInstanceField setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public FilterInstanceField setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public FilterInstanceField setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public FilterInstanceField setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public FilterInstanceField setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
