package ru.magnit.magreportbackend.service.domain.filter;

import ru.magnit.magreportbackend.dto.inner.olap.CubeData;
import ru.magnit.magreportbackend.dto.request.olap.FilterDefinition;

import java.util.Set;
import java.util.stream.Collectors;

public class CubeInListFilter implements CubeFilterNode{

    private boolean invert;
    private Set<String> values;
    private String[] data;

    @Override
    public CubeFilterNode init(CubeData sourceCube, FilterDefinition filterDefinition) {
        invert = filterDefinition.isInvertResult();
        values = filterDefinition.getValues().stream().map(String::intern).collect(Collectors.toSet());
        data = sourceCube.data()[sourceCube.fieldIndexes().get(filterDefinition.getFieldId())];
        return this;
    }

    @Override
    public boolean filter(int row) {
        return invert != values.contains(data[row]);
    }
}
