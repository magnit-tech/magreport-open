package ru.magnit.magreportbackend.service.domain;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.reportjob.ReportJob;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobUser;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobUserType;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobUserTypeEnum;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;
import ru.magnit.magreportbackend.mapper.auth.UserResponseMapper;
import ru.magnit.magreportbackend.repository.ReportJobUserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class ReportJobUserDomainService {

    private final ReportJobUserRepository repository;
    private final UserResponseMapper userResponseMapper;

    @Transactional
    public void addUsersJob( ReportJobUserTypeEnum type, Long jobId, List<UserResponse> users) {
        var result = users.stream()
                .map(user ->
                        new ReportJobUser()
                                .setReportJob(new ReportJob().setId(jobId))
                                .setUser(new User().setId(user.getId()))
                                .setType(new ReportJobUserType(type.getId()))
                )
                .toList();

        repository.saveAll(result);
    }

    @Transactional
    public void deleteUsersJob(ReportJobUserTypeEnum type, Long jobId, List<UserResponse> users) {
        users.forEach(user -> repository.findSharedJob(type.getId(), jobId, user.getId()).ifPresent(repository::delete));
    }

    @Transactional
    public List<UserResponse> getUsersJob(Long jobId){
       return userResponseMapper.from(repository.findAllForJob(jobId)
                .stream()
                .map(ReportJobUser::getUser)
                .toList());
    }
}
