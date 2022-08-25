package ru.magnit.magreportbackend.domain.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;
import ru.magnit.magreportbackend.domain.excel.UserReportExcelTemplate;
import ru.magnit.magreportbackend.domain.folderreport.FolderReport;
import ru.magnit.magreportbackend.domain.olap.OlapConfiguration;
import ru.magnit.magreportbackend.domain.olap.ReportOlapConfiguration;
import ru.magnit.magreportbackend.domain.reportjob.ReportJob;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobUser;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity(name = "USERS")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "USER_ID"))
public class User extends EntityWithName {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "PATRONYMIC")
    private String patronymic;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "EMAIL")
    private String email;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_STATUS_ID")
    private UserStatus userStatus;

    @OneToMany(cascade = ALL, mappedBy = "user")
    private List<UserRole> userRoles = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<UserReportExcelTemplate> userReportExcelTemplates = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<ReportJob> reportJobs = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<FolderReport> folderReports = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<OlapConfiguration> olapConfigurations = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    private List<ReportOlapConfiguration> authoredOlapConfigurations = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<ReportOlapConfiguration> setOlapConfigurations = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<ReportJobUser> reportJobUsers = Collections.emptyList();

    public User(Long userId) {
        this.id = userId;
    }

    @Override
    public User setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public User setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public User setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public User setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public User setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
