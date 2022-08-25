package ru.magnit.magreportbackend.mapper.olap;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.olap.OlapConfiguration;
import ru.magnit.magreportbackend.mapper.Cloner;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OlapConfigurationCloner implements Cloner<OlapConfiguration> {
    @Override
    public OlapConfiguration clone(OlapConfiguration source) {
        return new OlapConfiguration()
                .setData(source.getData())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setUser(source.getUser())
                .setCreatedDateTime(LocalDateTime.now())
                .setModifiedDateTime(LocalDateTime.now());
    }
}
