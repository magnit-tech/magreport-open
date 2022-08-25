package ru.magnit.magreportbackend.domain.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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

@Entity(name = "ROLE_TYPE")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "ROLE_TYPE_ID"))
public class RoleType extends EntityWithName {

    private static final long serialVersionUID = 1L;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleType")
    private List<Role> roles = Collections.emptyList();

    public RoleType(Long roleTypeId) {
        this.id = roleTypeId;
    }

    public RoleType(RoleTypeEnum roleType) {
        this.id = (long)roleType.ordinal();
    }

    @Override
    public RoleType setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public RoleType setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public RoleType setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public RoleType setCreatedDateTime(LocalDateTime createdDateTime) {

        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public RoleType setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
