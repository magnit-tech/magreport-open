package ru.magnit.magreportbackend.mapper.dataset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.domain.dataset.DataType;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetFieldAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class DataSetFieldMapper implements Mapper<DataSetField, DataSetFieldAddRequest> {

    @Override
    public DataSetField from(DataSetFieldAddRequest source) {
        return mapBaseProperties(source);
    }

    private DataSetField mapBaseProperties(DataSetFieldAddRequest source) {
        return new DataSetField()
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setIsSync(source.getIsValid())
                .setType(new DataType(source.getTypeId()));

    }
}
