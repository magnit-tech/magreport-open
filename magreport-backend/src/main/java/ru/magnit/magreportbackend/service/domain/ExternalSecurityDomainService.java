package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.inner.asm.ExternalAuthSecurityView;
import ru.magnit.magreportbackend.dto.request.asm.AsmSecurityAddRequest;
import ru.magnit.magreportbackend.dto.response.asm.AsmSecurityResponse;
import ru.magnit.magreportbackend.mapper.asm.AsmSecurityResponseMapper;
import ru.magnit.magreportbackend.mapper.asm.ExternalAuthMapper;
import ru.magnit.magreportbackend.mapper.asm.ExternalAuthMerger;
import ru.magnit.magreportbackend.mapper.asm.ExternalAuthSecurityViewMapper;
import ru.magnit.magreportbackend.repository.ExternalAuthRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExternalSecurityDomainService {

    private final ExternalAuthRepository authRepository;

    private final ExternalAuthMapper externalAuthMapper;
    private final AsmSecurityResponseMapper asmSecurityResponseMapper;
    private final ExternalAuthSecurityViewMapper externalAuthSecurityViewMapper;
    private final ExternalAuthMerger externalAuthMerger;

    @Transactional
    public Long addAsmSecurity(UserView currentUser, AsmSecurityAddRequest request) {
        final var externalAuth = externalAuthMapper.from(request);
        externalAuth.setUser(new User(currentUser.getId()));
        final var result = authRepository.save(externalAuth);

        return result.getId();
    }

    @Transactional
    public AsmSecurityResponse getAsmSecurity(Long id) {
        final var externalAuth = authRepository.getReferenceById(id);

        return asmSecurityResponseMapper.from(externalAuth);
    }

    @Transactional
    public List<AsmSecurityResponse> getAllAsmSecurity() {
        final var externalAuthList = authRepository.findAll();

        return asmSecurityResponseMapper.from(externalAuthList);
    }

    @Transactional
    public void deleteAsmSecurity(Long id) {

        authRepository.deleteById(id);
    }

    @Transactional
    public List<ExternalAuthSecurityView> getAllAmsFilters(List<Long> idList) {
        return externalAuthSecurityViewMapper.from(authRepository.findAllByIdIn(idList));
    }

    @Transactional
    public AsmSecurityResponse editAsmSecurity(AsmSecurityAddRequest request) {
        final var externalAuth = authRepository.getReferenceById(request.getId());
        final var mergedExternalAuth = externalAuthMerger.merge(externalAuth, request);

        final var savedExternalAuth = authRepository.saveAndFlush(mergedExternalAuth);

        return asmSecurityResponseMapper.from(savedExternalAuth);
    }
}
