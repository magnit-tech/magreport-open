package ru.magnit.magreportbackend.mapper.asm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSecurityFilter;
import ru.magnit.magreportbackend.dto.response.asm.AsmSecurityFilterResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterResponseMapper;

@Service
@RequiredArgsConstructor
public class AsmSecurityFilterResponseMapper implements Mapper<AsmSecurityFilterResponse, ExternalAuthSecurityFilter> {

    private final SecurityFilterResponseMapper securityFilterMapper;

    @Override
    public AsmSecurityFilterResponse from(ExternalAuthSecurityFilter source) {

        return new AsmSecurityFilterResponse(
                source.getId(),
                securityFilterMapper.from(source.getSecurityFilter()),
                source.getCreatedDateTime(),
                source.getModifiedDateTime());
    }
}
