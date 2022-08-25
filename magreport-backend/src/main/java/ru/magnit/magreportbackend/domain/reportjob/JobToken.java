package ru.magnit.magreportbackend.domain.reportjob;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity(name = "JOB_TOKEN")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class JobToken {
    @Id
    @Column(name = "JOB_TOKEN_ID")
    String token;

    @Column(name = "REPORT_JOB_ID")
    private Long reportJobId;

    @Column(name = "EXCEL_TEMPLATE_ID")
    private Long excelTemplateId;

    @Column(name="EMAIL")
    private String email;

    @Column(name="CREATED")
    private LocalDateTime created = LocalDateTime.now();

    public JobToken(String token, Long reportJobId, Long excelTemplateId, String email) {
        this.token = token;
        this.reportJobId = reportJobId;
        this.excelTemplateId = excelTemplateId;
        this.email = email;
    }
}
