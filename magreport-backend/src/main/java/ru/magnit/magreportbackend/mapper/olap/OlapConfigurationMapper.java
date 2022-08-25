package ru.magnit.magreportbackend.mapper.olap;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.olap.OlapConfiguration;
import ru.magnit.magreportbackend.dto.request.olap.OlapConfigUpdateRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OlapConfigurationMapper implements Mapper<OlapConfiguration, OlapConfigUpdateRequest> {
    @Override
    public OlapConfiguration from(OlapConfigUpdateRequest source) {
        return new OlapConfiguration()
                .setName(source.getOlapConfigName())
                .setDescription(source.getOlapConfigDescription())
                .setData(source.getOlapConfigData().toString())
                .setCreatedDateTime(LocalDateTime.now())
                .setModifiedDateTime(LocalDateTime.now());
    }
}
