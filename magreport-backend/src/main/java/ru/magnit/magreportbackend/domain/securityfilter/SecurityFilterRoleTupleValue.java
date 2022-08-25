package ru.magnit.magreportbackend.domain.securityfilter;

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

@Entity(name = "SECURITY_FILTER_TUPLE_VALUE")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "SECURITY_FILTER_TUPLE_VALUE_ID"))
public class SecurityFilterRoleTupleValue extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "VAL")
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SECURITY_FILTER_TUPLE_ID")
    private SecurityFilterRoleTuple tuple;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILTER_INSTANCE_FIELD_ID")
    private FilterInstanceField field;

    public SecurityFilterRoleTupleValue(Long id) {
        this.id = id;
    }

    @Override
    public SecurityFilterRoleTupleValue setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public SecurityFilterRoleTupleValue setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public SecurityFilterRoleTupleValue setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
