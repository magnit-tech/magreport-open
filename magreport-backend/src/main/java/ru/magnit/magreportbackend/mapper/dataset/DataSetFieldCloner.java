package ru.magnit.magreportbackend.mapper.dataset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.mapper.Cloner;

@Service
@RequiredArgsConstructor
public class DataSetFieldCloner implements Cloner <DataSetField> {
    @Override
    public DataSetField clone(DataSetField source) {
        return new DataSetField()
            .setName(source.getName())
            .setDescription(source.getDescription())
            .setType(source.getType())
            .setIsSync(source.getIsSync());
    }
}
