package ru.magnit.magreportbackend.mapper.serversettings;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.serversettings.ServerMailTemplateType;
import ru.magnit.magreportbackend.dto.response.serversettings.ServerMailTemplateTypeResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.auth.UserResponseMapper;

@Service
@RequiredArgsConstructor
public class ServerMailTemplateTypeResponseMapper implements Mapper<ServerMailTemplateTypeResponse, ServerMailTemplateType> {

    private final ServerMailTemplateResponseMapper templateMapper;
    private final UserResponseMapper userResponseMapper;

    @Override
    public ServerMailTemplateTypeResponse from(ServerMailTemplateType source) {
        return new ServerMailTemplateTypeResponse()
                .setId(source.getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setUser(userResponseMapper.from(source.getUser()))
                .setSystemMailTemplates(templateMapper.from(source.getServerMailTemplates()))
                .setCreated(source.getCreatedDateTime())
                .setModified(source.getModifiedDateTime());
    }
}
