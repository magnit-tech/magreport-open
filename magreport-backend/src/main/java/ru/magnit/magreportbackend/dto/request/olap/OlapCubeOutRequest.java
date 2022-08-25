package ru.magnit.magreportbackend.dto.request.olap;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.olap.PlacementType;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class OlapCubeOutRequest {

    private Long jobId;

    private Long cubeSize;

    private PlacementType metricPlacement;

    private FilterGroup filterGroup;

    private Interval columnsInterval;

    private Interval rowsInterval;

    private List<CubeField> fields = Collections.emptyList();

    private LinkedHashSet<Long> columnFields = new LinkedHashSet<>();

    private LinkedHashSet<Long> rowFields = new LinkedHashSet<>();

    private List<MetricDefinition> metrics = Collections.emptyList();

    private List<SortingParams> columnSort = Collections.emptyList();

    private List<SortingParams> rowSort = Collections.emptyList();


}
