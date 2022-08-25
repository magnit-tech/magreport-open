package ru.magnit.magreportbackend.domain.report;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;
import ru.magnit.magreportbackend.domain.FolderEntity;

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
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "REPORT_FOLDER")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "REPORT_FOLDER_ID"))
public class ReportFolder extends EntityWithName implements FolderEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    public ReportFolder(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private ReportFolder parentFolder;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentFolder")
    private List<ReportFolder> childFolders = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "folder")
    private List<Report> reports = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "folder")
    private List<ReportFolderRole> folderRoles = Collections.emptyList();

    @Override
    public ReportFolder setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ReportFolder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ReportFolder setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public ReportFolder setParentFolder(EntityWithName parentFolder) {
        this.parentFolder = (ReportFolder) parentFolder;
        return this;
    }

    @Override
    public ReportFolder setChildFolders(List<? extends EntityWithName> list) {
        this.childFolders = list.stream()
                .map(ReportFolder.class::cast)
                .collect(Collectors.toList());
        return this;
    }

    @Override
    public List<? extends EntityWithName> getChildElements() {
        return this.reports;
    }

    @Override
    public ReportFolder setChildElements(List<? extends EntityWithName> list) {
        this.reports = list.stream()
                .map(Report.class::cast)
                .collect(Collectors.toList());
        return this;
    }

    @Override
    public ReportFolder setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public ReportFolder setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
