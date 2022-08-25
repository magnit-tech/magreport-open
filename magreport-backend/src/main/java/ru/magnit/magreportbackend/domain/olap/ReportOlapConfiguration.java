package ru.magnit.magreportbackend.domain.olap;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.BaseEntity;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.reportjob.ReportJob;
import ru.magnit.magreportbackend.domain.user.User;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serial;
import java.time.LocalDateTime;

@Entity(name = "REPORT_OLAP_CONFIGURATION")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "REPORT_OLAP_CONFIGURATION_ID"))
public class ReportOlapConfiguration extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    public ReportOlapConfiguration(Long id) {
        this.id = id;
    }

    @Column(name = "IS_DEFAULT")
    private Boolean isDefault;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPORT_ID")
    private Report report;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPORT_JOB_ID")
    private ReportJob reportJob;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OLAP_CONFIGURATION_ID")
    private OlapConfiguration olapConfiguration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATOR_ID")
    private User creator;

    @Column(name = "IS_SHARED")
    private Boolean isShared;

    @Column(name = "IS_CURRENT")
    private Boolean isCurrent;

    @Override
    public ReportOlapConfiguration setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ReportOlapConfiguration setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public ReportOlapConfiguration setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
