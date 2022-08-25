package ru.magnit.magreportbackend.domain.filtertemplate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobFilter;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilter;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity(name = "FILTER_OPERATION_TYPE")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "FILTER_OPERATION_TYPE_ID"))
public class FilterOperationType extends EntityWithName {

    private static final long serialVersionUID = 1L;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "operationType")
    private List<SecurityFilter> securityFilters = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "type")
    private List<FilterOperation> operations = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "filterOperationType")
    private List<ReportJobFilter> reportJobFilters = Collections.emptyList();

    public FilterOperationTypeEnum getTypeEnum(){

        return FilterOperationTypeEnum.getById(id);
    }

    public FilterOperationType(Long id) {
        this.id = id;
    }

    public FilterOperationType(FilterOperationTypeEnum type) {
        this.id = (long) type.ordinal();
    }

    @Override
    public FilterOperationType setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public FilterOperationType setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public FilterOperationType setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public FilterOperationType setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public FilterOperationType setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
