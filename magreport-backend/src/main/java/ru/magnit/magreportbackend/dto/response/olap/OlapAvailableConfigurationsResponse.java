package ru.magnit.magreportbackend.dto.response.olap;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class OlapAvailableConfigurationsResponse {

    List<ReportOlapConfigResponse> commonReportConfigs = Collections.emptyList();
    List<ReportOlapConfigResponse> myReportConfigs = Collections.emptyList();
    List<ReportOlapConfigResponse> myJobConfig = Collections.emptyList();
    List<ReportOlapConfigResponse> sharedJobConfig = Collections.emptyList();
}
