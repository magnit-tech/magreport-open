package ru.magnit.magreportbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.service.domain.AdminDomainService;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminDomainService domainService;

    public byte[] getMainActiveLog() {
        return domainService.getMainLog();
    }

    public byte[] getOlapActiveLog() {
        return domainService.getOlapLog();
    }
}
