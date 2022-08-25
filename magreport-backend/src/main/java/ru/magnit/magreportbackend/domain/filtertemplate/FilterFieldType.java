package ru.magnit.magreportbackend.domain.filtertemplate;

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

@Entity(name = "FILTER_FIELD_TYPE")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "FILTER_FIELD_TYPE_ID"))
public class FilterFieldType extends EntityWithName {

    private static final long serialVersionUID = 1L;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "type")
    private List<FilterTemplateField> fields = Collections.emptyList();

    public FilterFieldType(Long id) {
        this.id = id;
    }

    public FilterFieldType(FilterFieldTypeEnum type) {
        this.id = (long) type.ordinal();
    }

    @Override
    public FilterFieldType setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public FilterFieldType setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public FilterFieldType setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public FilterFieldType setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public FilterFieldType setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
