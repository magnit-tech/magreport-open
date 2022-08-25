package ru.magnit.magreportbackend.domain.excel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;
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

import static javax.persistence.FetchType.LAZY;

@Entity(name = "EXCEL_TEMPLATE")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "EXCEL_TEMPLATE_ID"))
public class ExcelTemplate extends EntityWithName {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXCEL_TEMPLATE_FOLDER_ID")
    private ExcelTemplateFolder folder;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "excelTemplate")
    private List<ReportExcelTemplate> reportExcelTemplates = Collections.emptyList();

    public ExcelTemplate(Long id) {
        this.id = id;
    }

    @Override
    public ExcelTemplate setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ExcelTemplate setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ExcelTemplate setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public ExcelTemplate setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public ExcelTemplate setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
