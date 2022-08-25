package ru.magnit.magreportbackend.domain.datasource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity(name = "DATASOURCE_TYPE")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "DATASOURCE_TYPE_ID"))
public class DataSourceType extends EntityWithName {

    private static final long serialVersionUID = 1L;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "type")
    private List<DataSource> dataSources = Collections.emptyList();

    public DataSourceType(Long dataSourceTypeId) {
        this.id = dataSourceTypeId;
    }

    public DataSourceType(DataSourceTypeEnum dataSourceType) {
        this.id = (long) dataSourceType.ordinal();
    }

    @Override
    public DataSourceType setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public DataSourceType setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public DataSourceType setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public DataSourceType setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public DataSourceType setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
