package ru.magnit.magreportbackend.mapper.dataset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.dto.inner.dataset.DataSetFieldView;
import ru.magnit.magreportbackend.dto.inner.dataset.DataSetView;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceViewMapper;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataSetViewMapper implements Mapper<DataSetView, DataSet> {

    private final DataSetFieldViewMapper fieldViewMapper;
    private final DataSourceViewMapper dataSourceViewMapper;

    @Override
    public DataSetView from(DataSet source) {

        Map<Long, DataSetFieldView> fields = source.getFields()
                .stream()
                .map(fieldViewMapper::from)
                .collect(Collectors.toMap(DataSetFieldView::getId, Function.identity()));

        return new DataSetView(
                source.getId(),
                source.getName(),
                source.getSchemaName(),
                source.getObjectName(),
                dataSourceViewMapper.from(source.getDataSource()),
                fields);
    }
}
