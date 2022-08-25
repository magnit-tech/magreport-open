package ru.magnit.magreportbackend.dto.request.olap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.olap.AggregationType;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MetricDefinition {

    private Long fieldId;

    private AggregationType aggregationType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MetricDefinition that)) return false;
        return fieldId.equals(that.fieldId) && aggregationType == that.aggregationType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldId, aggregationType);
    }
}
