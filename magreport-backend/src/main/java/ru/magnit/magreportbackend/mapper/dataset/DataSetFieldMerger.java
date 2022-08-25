package ru.magnit.magreportbackend.mapper.dataset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetFieldAddRequest;
import ru.magnit.magreportbackend.mapper.Merger;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataSetFieldMerger implements Merger<List<DataSetField>, List<DataSetFieldAddRequest>> {

    private final DataSetFieldMapper fieldMapper;

    @Override
    public List<DataSetField> merge(List<DataSetField> target, List<DataSetFieldAddRequest> source) {
        markDeletedFields(target, source);
        addNewFields(target, source);

        return target;
    }

    private void addNewFields(List<DataSetField> target, List<DataSetFieldAddRequest> source) {
        var targetFieldNames = target
                .stream()
                .map(DataSetField::getName)
                .collect(Collectors.toSet());

        var newFields = source
                .stream()
                .filter(field -> !targetFieldNames.contains(field.getName()))
                .collect(Collectors.toList());

        target.addAll(fieldMapper.from(newFields));
    }

    private void markDeletedFields(List<DataSetField> target, List<DataSetFieldAddRequest> source) {
        var sourceFieldNames = source
                .stream()
                .map(DataSetFieldAddRequest::getName)
                .collect(Collectors.toSet());

        target
                .stream()
                .filter(field -> !sourceFieldNames.contains(field.getName()))
                .forEach(field -> field.setIsSync(false));
    }
}
