package ru.magnit.magreportbackend.mapper.filtertemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldType;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterFieldTypeResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FilterFieldTypeResponseMapper implements Mapper<FilterFieldTypeResponse, FilterFieldType> {

    @Override
    public FilterFieldTypeResponse from(FilterFieldType source) {

        return new FilterFieldTypeResponse(
                source.getId(),
                source.getName(),
                source.getDescription(),
                source.getCreatedDateTime(),
                source.getModifiedDateTime());
    }
}
