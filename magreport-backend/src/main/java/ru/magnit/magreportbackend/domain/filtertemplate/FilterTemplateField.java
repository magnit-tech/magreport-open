package ru.magnit.magreportbackend.domain.filtertemplate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity(name = "FILTER_TEMPLATE_FIELD")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "FILTER_TEMPLATE_FIELD_ID"))
public class FilterTemplateField extends EntityWithName {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILTER_TEMPLATE_ID", nullable = false)
    private FilterTemplate filterTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILTER_FIELD_TYPE_ID")
    private FilterFieldType type;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "templateField")
    private List<FilterInstanceField> instanceFields = new LinkedList<>();

    public FilterTemplateField(Long id) {
        this.id = id;
    }

    @Override
    public FilterTemplateField setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public FilterTemplateField setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public FilterTemplateField setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public FilterTemplateField setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public FilterTemplateField setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
