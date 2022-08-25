package ru.magnit.magreportbackend.domain.reportjob;

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

@Entity(name = "REPORT_JOB_USER_TYPE")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "REPORT_JOB_USER_TYPE_ID"))
public class ReportJobUserType  extends EntityWithName {

    private static final long serialVersionUID = 1L;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "type")
    private List<ReportJobUser> reportJobs = Collections.emptyList();

    public ReportJobUserType(Long id) {
        this.id = id;
    }

    @Override
    public ReportJobUserType setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ReportJobUserType setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ReportJobUserType setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public ReportJobUserType setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public ReportJobUserType setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }

}
