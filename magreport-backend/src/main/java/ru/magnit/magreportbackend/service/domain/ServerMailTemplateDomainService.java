package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.request.serversettings.ServerMailTemplateEditRequest;
import ru.magnit.magreportbackend.dto.response.serversettings.ServerMailTemplateResponse;
import ru.magnit.magreportbackend.mapper.serversettings.ServerMailTemplateResponseMapper;
import ru.magnit.magreportbackend.repository.ServerMailTemplateRepository;
import ru.magnit.magreportbackend.repository.ServerMailTemplateTypeRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServerMailTemplateDomainService {

    private final ServerMailTemplateRepository repository;
    private final ServerMailTemplateTypeRepository serverMailTemplateTypeRepository;
    private final ServerMailTemplateResponseMapper mapper;
    @Transactional
    public ServerMailTemplateResponse getServerMailTemplate (Long id) {
        return mapper.from(repository.getReferenceById(id));
    }
    @Transactional
    public void editServerMailTemplate (ServerMailTemplateEditRequest request,Long userId) {

        var template = repository.getReferenceById(request.getId());
        template.setName(request.getName());
        template.setDescription(request.getDescription());
        template.setSubject(request.getSubject());
        template.setBody(request.getBody());
        template.setModifiedDateTime(LocalDateTime.now());
        template.setUser(new User().setId(userId));


        repository.save(template);
    }
    @Transactional
    public List<ServerMailTemplateResponse> getServerMailTemplateByType (Long id) {
        var type = serverMailTemplateTypeRepository.getReferenceById(id);
        return mapper.from(type.getServerMailTemplates());
    }
    @Transactional
    public List<ServerMailTemplateResponse> getAllServerMailTemplate () {
        return mapper.from(repository.findAll());
    }
}
