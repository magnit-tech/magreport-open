package ru.magnit.magreportbackend.domain.filterreport;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity(name = "REPORT_FILTER_GROUP_OPERATION_TYPE")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "REPORT_FILTER_GROUP_OPERATION_TYPE_ID"))
public class GroupOperationType extends EntityWithName {

    private static final long serialVersionUID = 1L;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "type")
    private List<FilterReportGroup> groups = Collections.emptyList();

    public GroupOperationType(Long id) {
        this.id = id;
    }

    public GroupOperationType(GroupOperationTypeEnum operType) {
        this.id = (long) operType.ordinal();
    }

    @Override
    public GroupOperationType setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public GroupOperationType setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public GroupOperationType setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public GroupOperationType setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public GroupOperationType setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
