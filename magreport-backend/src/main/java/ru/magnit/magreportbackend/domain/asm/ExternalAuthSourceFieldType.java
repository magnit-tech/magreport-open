package ru.magnit.magreportbackend.domain.asm;

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
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity(name = "EXTERNAL_AUTH_SOURCE_FIELD_TYPE")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "EXTERNAL_AUTH_SOURCE_FIELD_TYPE_ID"))
public class ExternalAuthSourceFieldType extends EntityWithName {

    @Serial
    private static final long serialVersionUID = 1L;

    public ExternalAuthSourceFieldType(Long id) {
        this.id = id;
    }

    public ExternalAuthSourceFieldType(ExternalAuthSourceFieldTypeEnum type) {
        this.id = type.getId();
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "type")
    private List<ExternalAuthSourceField> fields = Collections.emptyList();

    @Override
    public ExternalAuthSourceFieldType setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ExternalAuthSourceFieldType setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ExternalAuthSourceFieldType setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public ExternalAuthSourceFieldType setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public ExternalAuthSourceFieldType setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
