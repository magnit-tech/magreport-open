package ru.magnit.magreportbackend.domain.asm;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.BaseEntity;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilter;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serial;
import java.time.LocalDateTime;

@Entity(name = "EXTERNAL_AUTH_SECURITY_FILTER")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "EXTERNAL_AUTH_SECURITY_FILTER_ID"))
public class ExternalAuthSecurityFilter extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXTERNAL_AUTH_SOURCE_ID")
    private ExternalAuthSource source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SECURITY_FILTER_ID")
    private SecurityFilter securityFilter;

    public ExternalAuthSecurityFilter(Long id) {
        this.id = id;
    }

    @Override
    public ExternalAuthSecurityFilter setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ExternalAuthSecurityFilter setCreatedDateTime(LocalDateTime created) {
        this.createdDateTime = created;
        return this;
    }

    @Override
    public ExternalAuthSecurityFilter setModifiedDateTime(LocalDateTime modified) {
        this.modifiedDateTime = modified;
        return this;
    }
}
