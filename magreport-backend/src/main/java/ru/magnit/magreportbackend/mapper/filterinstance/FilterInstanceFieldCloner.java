package ru.magnit.magreportbackend.mapper.filterinstance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;
import ru.magnit.magreportbackend.mapper.Cloner;


@Service
@RequiredArgsConstructor
public class FilterInstanceFieldCloner implements Cloner<FilterInstanceField> {
    @Override
    public FilterInstanceField clone(FilterInstanceField source) {
        return new FilterInstanceField()
            .setName(source.getName())
            .setDescription(source.getDescription())
            .setDataSetField(source.getDataSetField())
            .setExpand(source.getExpand())
            .setLevel(source.getLevel())
            .setTemplateField(source.getTemplateField());
    }
}
