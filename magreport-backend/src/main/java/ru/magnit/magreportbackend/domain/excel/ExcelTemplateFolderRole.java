package ru.magnit.magreportbackend.domain.excel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.BaseEntity;
import ru.magnit.magreportbackend.domain.user.Role;

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

@Entity(name = "EXCEL_TEMPLATE_FOLDER_ROLE")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "EXCEL_TEMPLATE_FOLDER_ROLE_ID"))
public class ExcelTemplateFolderRole extends BaseEntity {
    private static final long serialVersionUID = 1L;

    public ExcelTemplateFolderRole(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXCEL_TEMPLATE_FOLDER_ID")
    private ExcelTemplateFolder folder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ID")
    private Role role;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "folderRole")
    private List<ExcelTemplateFolderRolePermission> permissions = Collections.emptyList();

    @Override
    public ExcelTemplateFolderRole setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ExcelTemplateFolderRole setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public ExcelTemplateFolderRole setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
