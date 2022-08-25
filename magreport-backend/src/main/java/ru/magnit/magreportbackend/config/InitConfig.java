package ru.magnit.magreportbackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.magnit.magreportbackend.domain.user.UserRoleTypeEnum;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.dto.inner.UserInfo;
import ru.magnit.magreportbackend.service.domain.UserDomainService;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InitConfig {

    private final UserDomainService userDomainService;

    @Value("${superuser-param-name}")
    private String username;

    @Value("${superuser-param-email}")
    private String userEmail;

    @Value("${magreport.schedule-user}")
    private String scheduleUser;

    @PostConstruct
    public void initSuperUser() {

        var user = new UserInfo();

        user.setLoginName(username);
        user.setEmail(userEmail);
        user.setFirstName("");
        user.setLastName("");
        user.setPatronymic("");

        userDomainService.getOrCreateUserByName(user);
        var roles = userDomainService.getUserRoles(username, UserRoleTypeEnum.MANUAL).stream().map(RoleView::getName).collect(Collectors.toList());

        if (roles.stream().noneMatch(r -> r.equals("ADMIN"))) {
            roles.add("ADMIN");
            userDomainService.setUserRoles(username, roles);
        }
    }

    @PostConstruct
    public void initScheduleUser() {

        var user = new UserInfo();

        user.setLoginName(scheduleUser);
        user.setEmail("");
        user.setFirstName("");
        user.setLastName("");
        user.setPatronymic("");

        userDomainService.getOrCreateUserByName(user);
    }
}
