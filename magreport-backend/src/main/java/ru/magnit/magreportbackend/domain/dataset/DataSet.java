package ru.magnit.magreportbackend.domain.dataset;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSource;
import ru.magnit.magreportbackend.domain.datasource.DataSource;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstance;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterDataSet;
import ru.magnit.magreportbackend.domain.user.User;

import javax.persistence.AttributeOverride;
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

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity(name = "DATASET")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "DATASET_ID"))
public class DataSet extends EntityWithName {

    @Serial
    private static final long serialVersionUID = 1L;

    public DataSet(Long id) {
        this.id = id;
    }

    @Column(name = "CATALOG_NAME")
    private String catalogName;

    @Column(name = "SCHEMA_NAME")
    private String schemaName;

    @Column(name = "OBJECT_NAME")
    private String objectName;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "DATASET_FOLDER_ID")
    private DataSetFolder folder;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "DATASOURCE_ID")
    private DataSource dataSource;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "DATASET_TYPE_ID")
    private DataSetType type;

    @OneToMany(cascade = ALL, mappedBy = "dataSet")
    private List<DataSetField> fields = new LinkedList<>();

    @OneToMany(cascade = ALL, mappedBy = "dataSet")
    private List<ExternalAuthSource> externalAuthSources = new LinkedList<>();

    @OneToMany(cascade = ALL, mappedBy = "dataSet")
    private List<FilterInstance> filterInstances = new LinkedList<>();

    @OneToMany(cascade = ALL, mappedBy = "dataSet")
    private List<Report> reports = new LinkedList<>();

    @OneToMany(cascade = ALL, mappedBy = "dataSet")
    private List<SecurityFilterDataSet> securityFilterDataSets = Collections.emptyList();

    @Override
    public DataSet setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public DataSet setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public DataSet setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public DataSet setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public DataSet setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
