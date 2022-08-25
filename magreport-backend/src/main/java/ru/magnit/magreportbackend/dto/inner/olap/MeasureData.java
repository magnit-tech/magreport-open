package ru.magnit.magreportbackend.dto.inner.olap;

import java.util.List;
import java.util.Set;

public record MeasureData(
        Set<List<String>> values,
        int totalCount
) {
}
