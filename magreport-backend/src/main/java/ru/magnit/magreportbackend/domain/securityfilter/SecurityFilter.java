package ru.magnit.magreportbackend.domain.securityfilter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSecurityFilter;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstance;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationType;
import ru.magnit.magreportbackend.domain.user.User;

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

@Entity(name = "SECURITY_FILTER")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "SECURITY_FILTER_ID"))
public class SecurityFilter extends EntityWithName {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILTER_OPERATION_TYPE_ID")
    private FilterOperationType operationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILTER_INSTANCE_ID")
    private FilterInstance filterInstance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SECURITY_FILTER_FOLDER_ID")
    private SecurityFilterFolder folder;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "securityFilter")
    private List<ExternalAuthSecurityFilter> authSecurityFilters = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "securityFilter")
    private List<SecurityFilterDataSetField> fieldMappings = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "securityFilter")
    private List<SecurityFilterDataSet> dataSets = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "securityFilter")
    private List<SecurityFilterRole> filterRoles = Collections.emptyList();

    public SecurityFilter(Long id) {
        this.id = id;
    }

    @Override
    public SecurityFilter setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public SecurityFilter setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public SecurityFilter setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public SecurityFilter setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public SecurityFilter setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
