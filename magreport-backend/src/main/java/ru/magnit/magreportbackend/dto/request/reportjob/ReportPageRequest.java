package ru.magnit.magreportbackend.dto.request.reportjob;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class ReportPageRequest {

    private Long jobId;

    private Long pageNumber;

    private Long rowsPerPage;
}
