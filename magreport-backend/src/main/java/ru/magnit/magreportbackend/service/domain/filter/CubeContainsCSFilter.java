package ru.magnit.magreportbackend.service.domain.filter;

import ru.magnit.magreportbackend.dto.inner.olap.CubeData;
import ru.magnit.magreportbackend.dto.request.olap.FilterDefinition;

public class CubeContainsCSFilter implements CubeFilterNode {

    private boolean invert;
    private String value;
    private String[] data;


    @Override
    public CubeFilterNode init(CubeData sourceCube, FilterDefinition filterDefinition) {
        invert = filterDefinition.isInvertResult();
        value = filterDefinition.getValues().isEmpty() ? "" : filterDefinition.getValues().get(0).intern();
        data = sourceCube.data()[sourceCube.fieldIndexes().get(filterDefinition.getFieldId())];
        return this;
    }

    @Override
    public boolean filter(int row) {
        return invert != data[row].contains(value);
    }

}
