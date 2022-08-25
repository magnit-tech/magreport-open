package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.dto.response.serversettings.ServerMailTemplateTypeResponse;
import ru.magnit.magreportbackend.mapper.serversettings.ServerMailTemplateTypeResponseMapper;
import ru.magnit.magreportbackend.repository.ServerMailTemplateTypeRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServerMailTemplateTypeDomainService {

    private final ServerMailTemplateTypeRepository repository;
    private final ServerMailTemplateTypeResponseMapper mapper;

    @Transactional
    public ServerMailTemplateTypeResponse getServerMailTemplateType(Long id) {

        if (id == null)
            return new ServerMailTemplateTypeResponse()
                    .setName("root")
                    .setChildTypes(mapper.from(repository.findAll()));
        else
            return mapper.from(repository.getReferenceById(id));
    }

    @Transactional
    public List<ServerMailTemplateTypeResponse> getAllServerMailTemplateType() {
        return mapper.from(repository.findAll());
    }

}
