package ru.magnit.magreportbackend.dto.inner.dataset;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DataSetView {

    @EqualsAndHashCode.Include
    private Long id;

    private String name;

    private String schemaName;

    private String objectName;

    private DataSourceData dataSource;

    private Map<Long, DataSetFieldView> fields;

    public List<DataSetFieldView> getFields() {
        return new LinkedList<>(fields.values());
    }

    public DataSetFieldView getFieldById(Long fieldId) {
        return fields.get(fieldId);

    }
}
