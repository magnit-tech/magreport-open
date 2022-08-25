package ru.magnit.magreportbackend.dto.inner.olap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MetricResult {
    String value;
    int column;
    int row;
}
