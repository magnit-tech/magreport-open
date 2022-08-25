package ru.magnit.magreportbackend.domain.dataset;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceField;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;
import ru.magnit.magreportbackend.domain.report.ReportField;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterDataSetField;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "DATASET_FIELD")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "DATASET_FIELD_ID"))
public class DataSetField extends EntityWithName {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "IS_SYNC")
    private Boolean isSync;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "DATA_TYPE_ID")
    private DataType type;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "DATASET_ID")
    private DataSet dataSet;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dataSetField")
    private List<ExternalAuthSourceField> authSourceFields = new LinkedList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dataSetField")
    private List<FilterInstanceField> instanceFields = new LinkedList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dataSetField")
    private List<SecurityFilterDataSetField> fieldMappings = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dataSetField")
    private List<ReportField> reportFields = Collections.emptyList();

    public DataSetField(Long id) {
        this.id = id;
    }

    @Override
    public DataSetField setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public DataSetField setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public DataSetField setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public DataSetField setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public DataSetField setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
