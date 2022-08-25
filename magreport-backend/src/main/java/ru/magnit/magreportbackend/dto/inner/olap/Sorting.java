package ru.magnit.magreportbackend.dto.inner.olap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.magnit.magreportbackend.domain.olap.SortingOrder;

@Getter
@Setter
@AllArgsConstructor
public class Sorting {
    SortingOrder order;
    int tupleIndex;
    int metricId;
}
