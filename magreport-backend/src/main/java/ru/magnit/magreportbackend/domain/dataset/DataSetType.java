package ru.magnit.magreportbackend.domain.dataset;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity(name = "DATASET_TYPE")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "DATASET_TYPE_ID"))
public class DataSetType extends EntityWithName {

    @Serial
    private static final long serialVersionUID = 1L;

    @OneToMany(cascade = ALL, mappedBy = "type")
    private List<DataSet> dataSets = Collections.emptyList();

    public DataSetType(Long id) {
        this.id = id;
    }

    public DataSetType(DataSetTypeEnum dataSetType) {
        this.id = (long) dataSetType.ordinal();
    }

    @Override
    public DataSetType setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public DataSetType setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public DataSetType setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public DataSetType setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public DataSetType setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
