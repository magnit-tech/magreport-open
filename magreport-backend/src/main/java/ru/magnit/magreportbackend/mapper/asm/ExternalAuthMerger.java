package ru.magnit.magreportbackend.mapper.asm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.asm.ExternalAuth;
import ru.magnit.magreportbackend.domain.user.RoleType;
import ru.magnit.magreportbackend.dto.request.asm.AsmSecurityAddRequest;
import ru.magnit.magreportbackend.mapper.Merger;

@Service
@RequiredArgsConstructor
public class ExternalAuthMerger implements Merger<ExternalAuth, AsmSecurityAddRequest> {

    private final ExternalAuthSourceMapper sourceMapper;

    @Override
    public ExternalAuth merge(ExternalAuth target, AsmSecurityAddRequest source) {
        var merged = target == null ? new ExternalAuth() : target;

        var mappedSources = sourceMapper.from(source.getSecuritySources());
        mappedSources.forEach(ms -> ms.setExternalAuth(merged));

        return merged
                .setId(source.getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setRoleType(new RoleType(source.getRoleTypeId()))
                .setSources(mappedSources);
    }
}
