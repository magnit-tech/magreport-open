package ru.magnit.magreportbackend.domain.schedule;

import lombok.EqualsAndHashCode;
import lombok.Getter;
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

@Entity(name = "DESTINATION_TYPE")
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "DESTINATION_TYPE_ID"))
public class DestinationType extends EntityWithName {

    @Serial
    private static final long serialVersionUID = 1L;

    @OneToMany(cascade = ALL, mappedBy = "type")
    private List<DestinationEmail> emails = Collections.emptyList();

    @OneToMany(cascade = ALL, mappedBy = "type")
    private List<DestinationUser> users = Collections.emptyList();

    @OneToMany(cascade = ALL, mappedBy = "type")
    private List<DestinationRole> roles = Collections.emptyList();

    public DestinationType(Long id) {
        this.id = id;
    }

    public DestinationType() {
    }

    @Override
    public DestinationType setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public DestinationType setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public DestinationType setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public DestinationType setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public DestinationType setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }

}
