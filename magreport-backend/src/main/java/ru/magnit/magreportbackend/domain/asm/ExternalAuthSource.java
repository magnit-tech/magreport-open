package ru.magnit.magreportbackend.domain.asm;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;
import ru.magnit.magreportbackend.domain.dataset.DataSet;

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
import java.util.ArrayList;
import java.util.List;

@Entity(name = "EXTERNAL_AUTH_SOURCE")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "EXTERNAL_AUTH_SOURCE_ID"))
public class ExternalAuthSource extends EntityWithName {

    @Serial
    private static final long serialVersionUID = 1L;

    public ExternalAuthSource(Long id) {
        this.id = id;
    }

    @Column(name = "PRE_SQL")
    private String preSql;

    @Column(name = "POST_SQL")
    private String postSql;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXTERNAL_AUTH_ID")
    private ExternalAuth externalAuth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DATASET_ID")
    private DataSet dataSet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXTERNAL_AUTH_SOURCE_TYPE_ID")
    private ExternalAuthSourceType type;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "source", orphanRemoval = true)
    private List<ExternalAuthSourceField> fields = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "source", orphanRemoval = true)
    private List<ExternalAuthSecurityFilter> securityFilters = new ArrayList<>();

    public ExternalAuthSourceTypeEnum getTypeEnum() {

        return ExternalAuthSourceTypeEnum.getTypeById(type.getId());
    }

    @Override
    public ExternalAuthSource setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ExternalAuthSource setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ExternalAuthSource setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public ExternalAuthSource setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public ExternalAuthSource setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
