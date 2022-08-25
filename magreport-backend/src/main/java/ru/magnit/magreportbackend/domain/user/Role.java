package ru.magnit.magreportbackend.domain.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;
import ru.magnit.magreportbackend.domain.folderreport.FolderRole;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterRole;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity(name = "ROLE")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "ROLE_ID"))
public class Role extends EntityWithName {

    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_TYPE_ID")
    private RoleType roleType;

    @OneToMany(cascade = ALL, mappedBy = "role")
    private List<RoleDomainGroup> roleDomainGroups = Collections.emptyList();

    @OneToMany(cascade = ALL, mappedBy = "role")
    private List<UserRole> userRoles = Collections.emptyList();

    @OneToMany(cascade = ALL, mappedBy = "role")
    private List<FolderRole> folderRoles = Collections.emptyList();

    @OneToMany(cascade = ALL, mappedBy = "role")
    private List<SecurityFilterRole> securityFilterRoles = Collections.emptyList();

    public Role(Long roleId) {
        this.id = roleId;
    }

    @Override
    public Role setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public Role setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Role setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public Role setCreatedDateTime(LocalDateTime createdDateTime) {

        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public Role setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
