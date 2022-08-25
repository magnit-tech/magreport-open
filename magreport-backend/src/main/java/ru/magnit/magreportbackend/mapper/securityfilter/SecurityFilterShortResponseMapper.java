package ru.magnit.magreportbackend.mapper.securityfilter;

import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilter;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterShortResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

import java.util.LinkedList;

@Service
public class SecurityFilterShortResponseMapper implements Mapper<SecurityFilterShortResponse, SecurityFilter> {

    @Override
    public SecurityFilterShortResponse from(SecurityFilter source) {
        return new SecurityFilterShortResponse(
            source.getId(),
            source.getName(),
            source.getDescription(),
            source.getUser().getName(),
            source.getCreatedDateTime(),
            source.getModifiedDateTime(),
            new LinkedList<>());
    }
}
