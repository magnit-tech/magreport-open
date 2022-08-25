package ru.magnit.magreportbackend.mapper.asm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.asm.ExternalAuth;
import ru.magnit.magreportbackend.domain.user.RoleType;
import ru.magnit.magreportbackend.dto.request.asm.AsmSecurityAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ExternalAuthMapper implements Mapper<ExternalAuth, AsmSecurityAddRequest> {

    private final ExternalAuthSourceMapper sourceMapper;

    @Override
    public ExternalAuth from(AsmSecurityAddRequest source) {

        final var externalAuth = new ExternalAuth()
                .setId(source.getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setRoleType(new RoleType(source.getRoleTypeId()))
                .setSources(sourceMapper.from(source.getSecuritySources()));

        externalAuth.getSources().forEach(externalAuthSource -> externalAuthSource.setExternalAuth(externalAuth));

        return externalAuth;
    }
}
