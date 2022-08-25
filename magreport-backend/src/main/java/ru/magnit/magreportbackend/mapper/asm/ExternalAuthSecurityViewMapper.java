package ru.magnit.magreportbackend.mapper.asm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.asm.ExternalAuth;
import ru.magnit.magreportbackend.domain.user.RoleTypeEnum;
import ru.magnit.magreportbackend.dto.inner.asm.ExternalAuthSecurityView;
import ru.magnit.magreportbackend.mapper.Mapper;


@Service
@RequiredArgsConstructor
public class ExternalAuthSecurityViewMapper implements Mapper<ExternalAuthSecurityView, ExternalAuth> {

    private final ExternalAuthSourceViewMapper sourceViewMapper;

    @Override
    public ExternalAuthSecurityView from(ExternalAuth source) {

        return new ExternalAuthSecurityView()
                .setId(source.getId())
                .setRoleType(RoleTypeEnum.getByOrdinal(source.getRoleType().getId()))
                .setSources(sourceViewMapper.mapFrom(source.getSources()));
    }
}
