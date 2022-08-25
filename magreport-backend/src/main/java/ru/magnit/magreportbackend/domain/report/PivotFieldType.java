package ru.magnit.magreportbackend.domain.report;

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

@Entity(name = "PIVOT_FIELD_TYPE")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "PIVOT_FIELD_TYPE_ID"))
public class PivotFieldType extends EntityWithName {

    private static final long serialVersionUID = 1L;

    public PivotFieldType(Long id) {
        this.id = id;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pivotFieldType")
    private List<ReportField> fields = Collections.emptyList();

    @Override
    public PivotFieldType setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public PivotFieldType setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public PivotFieldType setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public PivotFieldType setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public PivotFieldType setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
