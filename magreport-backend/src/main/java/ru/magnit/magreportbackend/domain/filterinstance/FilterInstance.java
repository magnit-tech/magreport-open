package ru.magnit.magreportbackend.domain.filterinstance;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.filterreport.FilterReport;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplate;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilter;
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
import java.util.LinkedList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "FILTER_INSTANCE")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "FILTER_INSTANCE_ID"))
public class FilterInstance extends EntityWithName {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILTER_INSTANCE_FOLDER_ID")
    private FilterInstanceFolder folder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILTER_TEMPLATE_ID")
    private FilterTemplate filterTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DATASET_ID")
    private DataSet dataSet;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "instance")
    private List<FilterInstanceField> fields = new LinkedList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "filterInstance")
    private List<FilterReport> filterReports = new LinkedList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "filterInstance")
    private List<SecurityFilter> securityFilters = new LinkedList<>();

    @Column(name = "CODE")
    private String code;

    public FilterInstance(Long id) {
        this.id = id;
    }

    @Override
    public FilterInstance setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public FilterInstance setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public FilterInstance setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public FilterInstance setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public FilterInstance setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
