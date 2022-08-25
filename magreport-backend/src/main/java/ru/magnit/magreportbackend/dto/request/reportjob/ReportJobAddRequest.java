package ru.magnit.magreportbackend.dto.request.reportjob;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class ReportJobAddRequest {

    private Long reportId;

    private List<ReportJobFilterRequest> parameters;
}
