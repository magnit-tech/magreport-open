package ru.magnit.magreportbackend.mapper.olap;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.olap.OlapConfiguration;
import ru.magnit.magreportbackend.dto.response.olap.OlapConfigResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.auth.UserShortResponseMapper;

@Service
@RequiredArgsConstructor
public class OlapConfigurationResponseMapper implements Mapper<OlapConfigResponse, OlapConfiguration> {
    private final UserShortResponseMapper userShortResponseMapper;

    @Override
    public OlapConfigResponse from(OlapConfiguration source) {
        return new OlapConfigResponse()
                .setId(source.getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setData(source.getData())
                .setUser(userShortResponseMapper.from(source.getUser()))
                .setCreated(source.getCreatedDateTime())
                .setModified(source.getModifiedDateTime());
    }
}
