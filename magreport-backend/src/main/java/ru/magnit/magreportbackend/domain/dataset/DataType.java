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

@Entity(name = "DATA_TYPE")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "DATA_TYPE_ID"))
public class DataType extends EntityWithName {

    @Serial
    private static final long serialVersionUID = 1L;

    @OneToMany(cascade = ALL, mappedBy = "type")
    private List<DataSetField> fields = Collections.emptyList();

    public DataType(Long id) {
        this.id = id;
    }

    public DataType(DataTypeEnum dataSetType) {
        this.id = (long) dataSetType.ordinal();
    }

    public DataTypeEnum getEnum() {
        return DataTypeEnum.getTypeByOrdinal(id.intValue());
    }

    @Override
    public DataType setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public DataType setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public DataType setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public DataType setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public DataType setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
