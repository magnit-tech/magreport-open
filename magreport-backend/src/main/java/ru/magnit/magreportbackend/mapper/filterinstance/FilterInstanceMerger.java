package ru.magnit.magreportbackend.mapper.filterinstance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstance;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceFolder;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplate;
import ru.magnit.magreportbackend.dto.request.filterinstance.FilterInstanceAddRequest;
import ru.magnit.magreportbackend.mapper.Merger;

@Service
@RequiredArgsConstructor
public class FilterInstanceMerger implements Merger<FilterInstance, FilterInstanceAddRequest> {

    @Override
    public FilterInstance merge(FilterInstance target, FilterInstanceAddRequest source) {

        target
                .setName(source.getName())
                .setCode(source.getCode())
                .setDescription(source.getDescription())
                .setFolder(new FilterInstanceFolder(source.getFolderId()))
                .setDataSet(source.getDataSetId() == null ? null : new DataSet(source.getDataSetId()))
                .setFilterTemplate(new FilterTemplate(source.getTemplateId()));

        target.getFields().forEach(field -> field.setInstance(target));

        return target;
    }
}
