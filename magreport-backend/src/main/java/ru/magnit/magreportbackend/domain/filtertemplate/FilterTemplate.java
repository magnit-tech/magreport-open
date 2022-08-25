package ru.magnit.magreportbackend.domain.filtertemplate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstance;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.exception.InvalidParametersException;

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

import static javax.persistence.FetchType.LAZY;

@Entity(name = "FILTER_TEMPLATE")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "FILTER_TEMPLATE_ID"))
public class FilterTemplate extends EntityWithName {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILTER_TYPE_ID")
    private FilterType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILTER_TEMPLATE_FOLDER_ID")
    private FilterTemplateFolder folder;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "filterTemplate")
    private List<FilterTemplateField> fields = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "filterTemplate")
    private List<FilterOperation> operations = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "filterTemplate")
    private List<FilterInstance> filterInstances = Collections.emptyList();

    public FilterTemplate(Long id) {
        this.id = id;
    }

    @Override
    public FilterTemplate setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public FilterTemplate setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public FilterTemplate setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public FilterTemplate setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public FilterTemplate setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }

    public FilterTemplateField getFieldById(Long id) {

        return fields
                .stream()
                .filter(field -> field.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new InvalidParametersException("Field with id:" + id + " does not exists"));
    }
}
