package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.repository.DestinationEmailRepository;
import ru.magnit.magreportbackend.repository.DestinationRoleRepository;
import ru.magnit.magreportbackend.repository.DestinationUserRepository;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor


public class DestinationDomainService {

    private final DestinationEmailRepository repositoryEmail;
    private final DestinationUserRepository repositoryUser;
    private final DestinationRoleRepository repositoryRole;

    @Transactional
    public void deleteByScheduleTask(Long taskId){
        repositoryEmail.deleteByScheduleTaskId(taskId);
        repositoryUser.deleteByScheduleTaskId(taskId);
        repositoryRole.deleteByScheduleTaskId(taskId);
    }

}
