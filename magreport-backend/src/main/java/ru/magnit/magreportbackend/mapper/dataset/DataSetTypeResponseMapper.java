package ru.magnit.magreportbackend.mapper.dataset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSetType;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetTypeResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class DataSetTypeResponseMapper implements Mapper<DataSetTypeResponse, DataSetType> {

    @Override
    public DataSetTypeResponse from(DataSetType source) {

        return new DataSetTypeResponse(
                source.getId(),
                source.getName(),
                source.getDescription(),
                source.getCreatedDateTime(),
                source.getModifiedDateTime());
    }
}
