package ru.magnit.magreportbackend.domain.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "ROLE_DOMAIN_GROUP")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "ROLE_DOMAIN_GROUP_ID"))
public class RoleDomainGroup extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ROLE_ID")
    private Role role;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "DOMAIN_GROUP_ID")
    private DomainGroup domainGroup;

    public RoleDomainGroup(Long id) {
        this.id = id;
    }

    @Override
    public RoleDomainGroup setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public RoleDomainGroup setCreatedDateTime(LocalDateTime createdDateTime) {

        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public RoleDomainGroup setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
