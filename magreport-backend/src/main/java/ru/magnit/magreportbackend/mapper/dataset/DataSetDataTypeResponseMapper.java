package ru.magnit.magreportbackend.mapper.dataset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataType;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetDataTypeResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class DataSetDataTypeResponseMapper implements Mapper<DataSetDataTypeResponse, DataType> {

    @Override
    public DataSetDataTypeResponse from(DataType source) {

        return new DataSetDataTypeResponse()
                .setId(source.getId())
                .setName(source.getName())
                .setDescription(source.getDescription());
    }
}
