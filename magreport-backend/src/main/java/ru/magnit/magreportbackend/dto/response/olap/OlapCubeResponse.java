package ru.magnit.magreportbackend.dto.response.olap;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
public class OlapCubeResponse {

    private List<List<String>> columnValues;

    private List<List<String>> rowValues;

    private List<OlapMetricResponse> metricValues;

    private int totalColumns;

    private int totalRows;
}
