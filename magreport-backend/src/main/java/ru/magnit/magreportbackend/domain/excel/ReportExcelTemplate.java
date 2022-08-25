package ru.magnit.magreportbackend.domain.excel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.BaseEntity;
import ru.magnit.magreportbackend.domain.report.Report;

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

@Entity(name = "REPORT_EXCEL_TEMPLATE")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "REPORT_EXCEL_TEMPLATE_ID"))
public class ReportExcelTemplate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXCEL_TEMPLATE_ID")
    private ExcelTemplate excelTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPORT_ID")
    private Report report;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reportExcelTemplate")
    private List<UserReportExcelTemplate> userReportExcelTemplates = Collections.emptyList();

    @Column(name = "IS_DEFAULT")
    private Boolean isDefault;

    public ReportExcelTemplate(Long id) {
        this.id = id;
    }

    @Override
    public ReportExcelTemplate setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ReportExcelTemplate setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public ReportExcelTemplate setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
