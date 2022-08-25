package ru.magnit.magreportbackend.mapper.asm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceField;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceFieldTypeEnum;
import ru.magnit.magreportbackend.dto.inner.asm.ExternalAuthSourceFieldView;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetFieldViewMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExternalAuthSourceFieldViewMapper implements Mapper<ExternalAuthSourceFieldView, ExternalAuthSourceField> {

    private final DataSetFieldViewMapper dataSetFieldViewMapper;

    @Override
    public ExternalAuthSourceFieldView from(ExternalAuthSourceField source) {
        return mapBaseProperties(source);
    }

    public Map<ExternalAuthSourceFieldTypeEnum, List<ExternalAuthSourceFieldView>> mapFrom(List<ExternalAuthSourceField> amsSecuritySourceFields) {
        return amsSecuritySourceFields
                .stream()
                .map(this::from)
                .collect(Collectors.groupingBy(ExternalAuthSourceFieldView::getType));
    }

    private ExternalAuthSourceFieldView mapBaseProperties(ExternalAuthSourceField source){
        var result = new ExternalAuthSourceFieldView();
        result.setId(source.getId());
        result.setType(source.getTypeEnum());
        result.setDataSetField(dataSetFieldViewMapper.from(source.getDataSetField()));

        return result;
    }
}
