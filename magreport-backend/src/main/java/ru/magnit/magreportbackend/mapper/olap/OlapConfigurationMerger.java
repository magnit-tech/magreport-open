package ru.magnit.magreportbackend.mapper.olap;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.olap.OlapConfiguration;
import ru.magnit.magreportbackend.dto.request.olap.OlapConfigUpdateRequest;
import ru.magnit.magreportbackend.mapper.Merger;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OlapConfigurationMerger implements Merger<OlapConfiguration, OlapConfigUpdateRequest> {
    @Override
    public OlapConfiguration merge(OlapConfiguration target, OlapConfigUpdateRequest source) {

        return target
                .setData(source.getOlapConfigData().toString())
                .setName(source.getOlapConfigName())
                .setDescription(source.getOlapConfigDescription())
                .setModifiedDateTime(LocalDateTime.now());

    }
}
