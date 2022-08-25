package ru.magnit.magreportbackend.domain.asm;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.BaseEntity;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serial;
import java.time.LocalDateTime;

@Entity(name = "EXTERNAL_AUTH_SOURCE_FIELD_FI_FIELD")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "EXTERNAL_AUTH_SOURCE_FIELD_FI_FIELD_ID"))
public class ExternalAuthSourceFieldFilterInstanceField extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXTERNAL_AUTH_SOURCE_FIELD_ID")
    private ExternalAuthSourceField sourceField;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILTER_INSTANCE_FIELD_ID")
    private FilterInstanceField filterInstanceField;

    public ExternalAuthSourceFieldFilterInstanceField(Long id) {
        this.id = id;
    }

    @Override
    public ExternalAuthSourceFieldFilterInstanceField setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ExternalAuthSourceFieldFilterInstanceField setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public ExternalAuthSourceFieldFilterInstanceField setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
