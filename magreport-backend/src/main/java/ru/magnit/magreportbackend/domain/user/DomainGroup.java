package ru.magnit.magreportbackend.domain.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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

@Entity(name = "DOMAIN_GROUP")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "DOMAIN_GROUP_ID"))
@Schema(name = "Справочник доменных групп")
public class DomainGroup extends EntityWithName {

    @Serial
    private static final long serialVersionUID = 1L;

    @OneToMany(cascade = ALL, mappedBy = "domainGroup")
    @Schema(name = "Привязки доменных групп к ролям системы")
    private List<RoleDomainGroup> roleDomainGroups = Collections.emptyList();

    public DomainGroup(Long domainGroupId) {
        this.id = domainGroupId;
    }

    @Override
    public DomainGroup setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public DomainGroup setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public DomainGroup setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public DomainGroup setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public DomainGroup setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
