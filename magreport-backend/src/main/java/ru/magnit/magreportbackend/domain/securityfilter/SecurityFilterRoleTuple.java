package ru.magnit.magreportbackend.domain.securityfilter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity(name = "SECURITY_FILTER_TUPLE")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "SECURITY_FILTER_TUPLE_ID"))
public class SecurityFilterRoleTuple extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SECURITY_FILTER_ROLE_ID")
    private SecurityFilterRole securityFilterRole;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tuple")
    private List<SecurityFilterRoleTupleValue> tupleValues = Collections.emptyList();

    public SecurityFilterRoleTuple(Long id) {
        this.id = id;
    }

    @Override
    public SecurityFilterRoleTuple setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public SecurityFilterRoleTuple setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public SecurityFilterRoleTuple setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
