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

@Entity(name = "EXTERNAL_AUTH_SOURCE_TYPE")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "EXTERNAL_AUTH_SOURCE_TYPE_ID"))
public class ExternalAuthSourceType extends EntityWithName {

    @Serial
    private static final long serialVersionUID = 1L;

    public ExternalAuthSourceType(Long id) {
        this.id = id;
    }

    public ExternalAuthSourceType(ExternalAuthSourceTypeEnum type) {
        this.id = type.getId();
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "type")
    private List<ExternalAuthSource> sources = Collections.emptyList();

    @Override
    public ExternalAuthSourceType setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ExternalAuthSourceType setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ExternalAuthSourceType setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public ExternalAuthSourceType setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public ExternalAuthSourceType setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
