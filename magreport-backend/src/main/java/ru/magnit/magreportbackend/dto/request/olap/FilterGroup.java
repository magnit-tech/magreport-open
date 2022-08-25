package ru.magnit.magreportbackend.dto.request.olap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.filterreport.GroupOperationTypeEnum;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class FilterGroup {
    private GroupOperationTypeEnum operationType;

    private boolean invertResult;

    private List<FilterGroup> childGroups = Collections.emptyList();

    private List<FilterDefinition> filters = Collections.emptyList();

    public Set<Long> getAllFieldIds(){
        var result = filters.stream().map(FilterDefinition::getFieldId).collect(Collectors.toCollection(HashSet::new));
        result.addAll(childGroups.stream().flatMap(g -> g.getAllFieldIds().stream()).collect(Collectors.toSet()));
        return result;
    }
}
