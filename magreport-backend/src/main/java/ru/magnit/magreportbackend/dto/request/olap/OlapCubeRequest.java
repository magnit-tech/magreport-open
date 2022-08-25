package ru.magnit.magreportbackend.dto.request.olap;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.olap.PlacementType;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class OlapCubeRequest {

    private Long jobId;

    private LinkedHashSet<Long> columnFields = new LinkedHashSet<>();

    private LinkedHashSet<Long> rowFields = new LinkedHashSet<>();

    private List<MetricDefinition> metrics = Collections.emptyList();

    private PlacementType metricPlacement;

    private FilterGroup filterGroup;

    private MetricFilterGroup metricFilterGroup;

    private Interval columnsInterval;

    private Interval rowsInterval;

    private List<SortingParams> columnSort = Collections.emptyList();

    private List<SortingParams> rowSort = Collections.emptyList();

    public Set<Long> getAllFieldIds(){
        final var result = new HashSet<Long>();

        result.addAll(columnFields);
        result.addAll(rowFields);
        result.addAll(metrics.stream().map(MetricDefinition::getFieldId).toList());

        return result;
    }
}
