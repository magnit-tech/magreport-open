package ru.magnit.magreportbackend.domain.olap;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;
import ru.magnit.magreportbackend.domain.user.User;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.io.Serial;
import java.time.LocalDateTime;

@Entity(name = "OLAP_CONFIGURATION")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "OLAP_CONFIGURATION_ID"))
public class OlapConfiguration extends EntityWithName {
    @Serial
    private static final long serialVersionUID = 1L;

    @Lob
    @Column(name = "DATA")
    private String data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    public OlapConfiguration(Long id) {
        this.id = id;
    }

    @Override
    public OlapConfiguration setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public OlapConfiguration setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public OlapConfiguration setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public OlapConfiguration setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public OlapConfiguration setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
