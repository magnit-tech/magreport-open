package ru.magnit.magreportbackend.domain.filtertemplate;

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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "FILTER_TEMPLATE_FOLDER")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "FILTER_TEMPLATE_FOLDER_ID"))
public class FilterTemplateFolder extends EntityWithName implements FolderEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private FilterTemplateFolder parentFolder;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentFolder")
    private List<FilterTemplateFolder> childFolders = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "folder")
    private List<FilterTemplate> filterTemplates = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "folder")
    private List<FilterTemplateFolderRole> folderRoles = new LinkedList<>();

    public FilterTemplateFolder(Long id) {
        this.id = id;
    }

    @Override
    public FilterTemplateFolder setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public FilterTemplateFolder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public FilterTemplateFolder setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public FilterTemplateFolder setParentFolder(EntityWithName parentFolder) {
        this.parentFolder = (FilterTemplateFolder) parentFolder;
        return this;
    }

    @Override
    public FilterTemplateFolder setChildFolders(List<? extends EntityWithName> list) {
        this.childFolders = list.stream()
                .map(FilterTemplateFolder.class::cast)
                .collect(Collectors.toList());
        return this;
    }

    @Override
    public List<? extends EntityWithName> getChildElements() {
        return this.filterTemplates;
    }

    @Override
    public FilterTemplateFolder setChildElements(List<? extends EntityWithName> list) {
        this.filterTemplates = list.stream()
                .map(FilterTemplate.class::cast)
                .collect(Collectors.toList());
        return this;
    }

    @Override
    public FilterTemplateFolder setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public FilterTemplateFolder setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
