package ru.magnit.magreportbackend.mapper.filterinstance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstance;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceShortResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FilterInstanceShortResponseMapper implements Mapper<FilterInstanceShortResponse, FilterInstance> {
    @Override
    public FilterInstanceShortResponse from(FilterInstance source) {
        return new FilterInstanceShortResponse(
                source.getId(),
                source.getName()
        );
    }
}
