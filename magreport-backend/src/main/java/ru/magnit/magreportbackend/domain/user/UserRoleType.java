package ru.magnit.magreportbackend.domain.user;

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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity(name = "USER_ROLE_TYPE")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "USER_ROLE_TYPE_ID"))
public class UserRoleType extends EntityWithName {

    private static final long serialVersionUID = 1L;

    @OneToMany(mappedBy = "userRoleType")
    private List<UserRole> userRoles = Collections.emptyList();

    public UserRoleType(Long userRoleTypeId) {
        this.id = userRoleTypeId;
    }

    public UserRoleType(UserRoleTypeEnum userRoleType) {
        this.id = (long)userRoleType.ordinal();
    }

    @Override
    public UserRoleType setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public UserRoleType setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public UserRoleType setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public UserRoleType setCreatedDateTime(LocalDateTime createdDateTime) {

        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public UserRoleType setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
