package ru.magnit.magreportbackend.domain.report;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.excel.ReportExcelTemplate;
import ru.magnit.magreportbackend.domain.excel.UserReportExcelTemplate;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportGroup;
import ru.magnit.magreportbackend.domain.folderreport.FolderReport;
import ru.magnit.magreportbackend.domain.reportjob.ReportJob;
import ru.magnit.magreportbackend.domain.user.User;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "REPORT")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "REPORT_ID"))
public class Report extends EntityWithName {

    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "REQUIREMENTS_URL")
    private String requirementsLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPORT_FOLDER_ID")
    private ReportFolder folder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DATASET_ID")
    private DataSet dataSet;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "report")
    private List<ReportField> fields = new LinkedList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "report")
    private List<ReportExcelTemplate> reportExcelTemplates = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "report")
    private List<UserReportExcelTemplate> userReportExcelTemplates = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "report")
    private List<FolderReport> folderReports = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "report")
    private List<ReportJob> reportJobs = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "report")
    private List<FilterReportGroup> filterReportGroups = Collections.emptyList();

    public Report(Long id) {
        this.id = id;
    }

    @Override
    public Report setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public Report setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Report setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public Report setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public Report setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
