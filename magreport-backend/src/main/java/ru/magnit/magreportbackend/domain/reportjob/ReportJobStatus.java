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

@Entity(name = "REPORT_JOB_STATUS")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "REPORT_JOB_STATUS_ID"))
public class ReportJobStatus extends EntityWithName {

    private static final long serialVersionUID = 1L;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "status")
    private List<ReportJob> reportJobs = Collections.emptyList();

    public ReportJobStatus(Long id) {
        this.id = id;
    }

    @Override
    public ReportJobStatus setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ReportJobStatus setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ReportJobStatus setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public ReportJobStatus setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public ReportJobStatus setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
