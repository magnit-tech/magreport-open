package ru.magnit.magreportbackend.mapper.asm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.asm.ExternalAuth;
import ru.magnit.magreportbackend.dto.response.asm.AsmSecurityResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.auth.RoleTypeResponseMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AsmSecurityResponseMapper implements Mapper<AsmSecurityResponse, ExternalAuth> {

    private final AsmSecuritySourceResponseMapper sourceResponseMapper;
    private final RoleTypeResponseMapper roleTypeResponseMapper;

    @Override
    public AsmSecurityResponse from(ExternalAuth source) {

        return new AsmSecurityResponse(
                source.getId(),
                source.getName(),
                source.getDescription(),
                sourceResponseMapper.from(source.getSources()),
                source.getUser().getName(),
                roleTypeResponseMapper.shallowMap(source.getRoleType()),
                source.getCreatedDateTime(),
                source.getModifiedDateTime());
    }

    @Override
    public List<AsmSecurityResponse> from(List<ExternalAuth> sources) {
        return sources.stream().map(this::from).collect(Collectors.toList());
    }
}
