package ru.magnit.magreportbackend.mapper.dataset;

import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.dto.inner.dataset.DataSetFieldView;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
public class DataSetFieldViewMapper implements Mapper<DataSetFieldView, DataSetField> {

    @Override
    public DataSetFieldView from(DataSetField source) {

        return new DataSetFieldView(
                source.getId(),
                source.getType().getEnum(),
                source.getName(),
                source.getDescription(),
                source.getIsSync());
    }
}
