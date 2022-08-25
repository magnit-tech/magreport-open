package ru.magnit.magreportbackend.domain.asm;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.BaseEntity;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;

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

@Entity(name = "EXTERNAL_AUTH_SOURCE_FIELD")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "EXTERNAL_AUTH_SOURCE_FIELD_ID"))
public class ExternalAuthSourceField extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    public ExternalAuthSourceField(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXTERNAL_AUTH_SOURCE_ID")
    private ExternalAuthSource source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXTERNAL_AUTH_SOURCE_FIELD_TYPE_ID")
    private ExternalAuthSourceFieldType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DATASET_FIELD_ID")
    private DataSetField dataSetField;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sourceField", orphanRemoval = true)
    private List<ExternalAuthSourceFieldFilterInstanceField> filterInstanceFields = new ArrayList<>();

    public ExternalAuthSourceFieldTypeEnum getTypeEnum() {

        return ExternalAuthSourceFieldTypeEnum.getById(type.getId());
    }

    @Override
    public ExternalAuthSourceField setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ExternalAuthSourceField setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public ExternalAuthSourceField setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
