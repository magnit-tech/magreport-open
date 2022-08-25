package ru.magnit.magreportbackend.mapper.serversettings;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.serversettings.ServerMailTemplate;
import ru.magnit.magreportbackend.domain.serversettings.ServerMailTemplateTypeEnum;
import ru.magnit.magreportbackend.dto.response.serversettings.ServerMailTemplateResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.auth.UserResponseMapper;

@Service
@RequiredArgsConstructor
public class ServerMailTemplateResponseMapper implements Mapper <ServerMailTemplateResponse, ServerMailTemplate> {

    private final UserResponseMapper userResponseMapper;

    @Override
    public ServerMailTemplateResponse from(ServerMailTemplate source) {
        return new ServerMailTemplateResponse()
                .setId(source.getId())
                .setType(ServerMailTemplateTypeEnum.getById(source.getType().getId()))
                .setCode(source.getCode())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setSubject(source.getSubject())
                .setBody(source.getBody())
                .setUser(userResponseMapper.from(source.getUser()))
                .setCreated(source.getCreatedDateTime())
                .setModified(source.getModifiedDateTime());
    }
}
