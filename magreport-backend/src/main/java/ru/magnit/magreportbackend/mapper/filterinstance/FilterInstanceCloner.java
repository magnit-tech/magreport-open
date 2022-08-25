package ru.magnit.magreportbackend.mapper.filterinstance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstance;
import ru.magnit.magreportbackend.mapper.Cloner;

@Service
@RequiredArgsConstructor
public class FilterInstanceCloner implements Cloner<FilterInstance> {

    private final FilterInstanceFieldCloner fieldCloner;

    @Override
    public FilterInstance clone(FilterInstance source) {
        final var filterInstance = new FilterInstance()
            .setName(source.getName())
            .setDescription(source.getDescription())
            .setCode(source.getCode())
            .setDataSet(source.getDataSet())
            .setFilterTemplate(source.getFilterTemplate());

        final var fields = fieldCloner.clone(source.getFields());
        fields.forEach(field -> field.setInstance(filterInstance));
        filterInstance.setFields(fields);

        return filterInstance;
    }
}
