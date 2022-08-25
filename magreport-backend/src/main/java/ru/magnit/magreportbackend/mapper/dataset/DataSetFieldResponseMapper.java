package ru.magnit.magreportbackend.mapper.dataset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetFieldResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class DataSetFieldResponseMapper implements Mapper<DataSetFieldResponse, DataSetField> {

    @Override
    public DataSetFieldResponse from(DataSetField source) {
        return mapBaseProperties(source);
    }

    private DataSetFieldResponse mapBaseProperties(DataSetField source) {
        return new DataSetFieldResponse()
                .setId(source.getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setTypeId(source.getType().getId())
                .setTypeName(source.getType().getName())
                .setIsValid(source.getIsSync());
    }
}
