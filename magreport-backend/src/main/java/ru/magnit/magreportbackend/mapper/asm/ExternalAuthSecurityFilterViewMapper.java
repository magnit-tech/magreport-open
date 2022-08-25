package ru.magnit.magreportbackend.mapper.asm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSecurityFilter;
import ru.magnit.magreportbackend.dto.inner.asm.ExternalAuthSecurityFilterView;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetViewMapper;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterDataFIMapper;


@Service
@RequiredArgsConstructor
public class ExternalAuthSecurityFilterViewMapper implements Mapper<ExternalAuthSecurityFilterView, ExternalAuthSecurityFilter> {

    private final DataSetViewMapper dataSetViewMapper;
    private final FilterDataFIMapper filterDataFIMapper;

    @Override
    public ExternalAuthSecurityFilterView from(ExternalAuthSecurityFilter source) {
        return new ExternalAuthSecurityFilterView(
                source.getId(),
                source.getSecurityFilter().getId(),
                dataSetViewMapper.from(source.getSecurityFilter().getFilterInstance().getDataSet()),
                filterDataFIMapper.from(source.getSecurityFilter().getFilterInstance())
        );
    }
}
