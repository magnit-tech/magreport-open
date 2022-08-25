package ru.magnit.magreportbackend.mapper.asm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSecurityFilter;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSource;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceTypeEnum;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.dto.inner.asm.ExternalAuthSecurityFilterView;
import ru.magnit.magreportbackend.dto.inner.asm.ExternalAuthSourceView;
import ru.magnit.magreportbackend.dto.inner.dataset.DataSetView;
import ru.magnit.magreportbackend.mapper.Mapper;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExternalAuthSourceViewMapper implements Mapper<ExternalAuthSourceView, ExternalAuthSource> {

    private final Mapper<DataSetView, DataSet> dataSetViewMapper;
    private final ExternalAuthSourceFieldViewMapper fieldViewMapper;
    private final Mapper<ExternalAuthSecurityFilterView, ExternalAuthSecurityFilter> amsFilterViewMapper;

    @Override
    public ExternalAuthSourceView from(ExternalAuthSource source) {
        return mapBaseProperties(source);
    }

    public Map<ExternalAuthSourceTypeEnum, ExternalAuthSourceView> mapFrom(List<ExternalAuthSource> amsSecuritySources) {
        return amsSecuritySources
                .stream()
                .map(this::from)
                .collect(Collectors.toMap(ExternalAuthSourceView::getType, Function.identity()));
    }

    private ExternalAuthSourceView mapBaseProperties(ExternalAuthSource source) {
        return new ExternalAuthSourceView()
                .setId(source.getId())
                .setType(source.getTypeEnum())
                .setDataSet(dataSetViewMapper.from(source.getDataSet()))
                .setFields(fieldViewMapper.mapFrom(source.getFields()))
                .setFilters(amsFilterViewMapper.from(source.getSecurityFilters()))
                .setPreSql(source.getPreSql())
                .setPostSql(source.getPostSql());
    }

}
