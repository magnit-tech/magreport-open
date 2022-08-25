package ru.magnit.magreportbackend.mapper.filtertemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationType;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterOperationTypeResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FilterOperationTypeResponseMapper implements Mapper<FilterOperationTypeResponse, FilterOperationType> {

    @Override
    public FilterOperationTypeResponse from(FilterOperationType source) {

        return new FilterOperationTypeResponse(
                source.getId(),
                source.getName(),
                source.getDescription(),
                source.getCreatedDateTime(),
                source.getModifiedDateTime());
    }
}
