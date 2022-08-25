package ru.magnit.magreportbackend.domain.asm;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;
import ru.magnit.magreportbackend.domain.user.RoleType;
import ru.magnit.magreportbackend.domain.user.User;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
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

import static javax.persistence.FetchType.LAZY;

@Entity(name = "EXTERNAL_AUTH")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "EXTERNAL_AUTH_ID"))
public class ExternalAuth extends EntityWithName {

    @Serial
    private static final long serialVersionUID = 1L;

    public ExternalAuth(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_TYPE_ID")
    private RoleType roleType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "externalAuth")
    private List<ExternalAuthSource> sources = Collections.emptyList();

    @Override
    public ExternalAuth setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ExternalAuth setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ExternalAuth setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public ExternalAuth setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public ExternalAuth setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
