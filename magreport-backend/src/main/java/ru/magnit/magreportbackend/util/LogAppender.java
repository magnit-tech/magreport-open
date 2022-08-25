package ru.magnit.magreportbackend.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import ru.magnit.magreportbackend.dto.request.user.RoleRequest;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;
import ru.magnit.magreportbackend.service.EmailService;
import ru.magnit.magreportbackend.service.RoleService;

import javax.mail.Message;
import java.util.Collections;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class LogAppender extends UnsynchronizedAppenderBase<ILoggingEvent> implements SmartLifecycle {

    private final EmailService emailService;
    private final RoleService roleService;

    @Override
    protected void append(ILoggingEvent event) {

        if (event.getLevel() != Level.ERROR)
            return;

        var stackTrace = new StringBuilder();
        for (StackTraceElement element : event.getCallerData()) {
            stackTrace.append(element);
            stackTrace.append("</br>");
        }

        var mes = String.format(
                "<html><body>" +
                        "<b> Error: %s </b>" +
                        "<br/>" +
                        "<b>StateTrace :</b></br> %s", event.getMessage(), stackTrace);

        var response = roleService.getRoleUsers(new RoleRequest().setId(0L));
        var list = response.getUsers().stream().map(UserResponse::getEmail).collect(Collectors.toList());

        emailService.sendMailToList(Message.RecipientType.TO,list, "LogBack: Магрепорт 2.0 ", mes, Collections.emptyList());
    }

    @Override
    public boolean isRunning() {
        return isStarted();
    }
}
