package ru.magnit.magreportbackend.domain.filtertemplate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity(name = "FILTER_TEMPLATE_OPERATION")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "FILTER_TEMPLATE_OPERATION_ID"))
public class FilterOperation extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILTER_TEMPLATE_ID")
    private FilterTemplate filterTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILTER_OPERATION_TYPE_ID")
    private FilterOperationType type;

    public FilterOperation(Long id) {
        this.id = id;
    }

    @Override
    public FilterOperation setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public FilterOperation setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public FilterOperation setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
