package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.schedule.DestinationTypeEnum;
import ru.magnit.magreportbackend.dto.request.user.RoleRequest;
import ru.magnit.magreportbackend.dto.request.user.UserRequest;
import ru.magnit.magreportbackend.dto.response.schedule.DestinationEmailResponse;
import ru.magnit.magreportbackend.dto.response.schedule.DestinationRoleResponse;
import ru.magnit.magreportbackend.dto.response.schedule.DestinationUserResponse;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleTaskResponse;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;
import ru.magnit.magreportbackend.repository.ServerMailTemplateRepository;
import ru.magnit.magreportbackend.service.EmailService;
import ru.magnit.magreportbackend.service.RoleService;
import ru.magnit.magreportbackend.service.UserService;
import ru.magnit.magreportbackend.util.Pair;

import javax.mail.Message;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailTextDomainService {

    private final ServerMailTemplateRepository repository;
    private final EmailService emailService;
    private final UserService userService;
    private final RoleService roleService;
    private final JobTokenDomainService jobTokenDomainService;


    @Value("${mail.adminMailBox}")
    private String adminMailBox;

    @Value("${magreport.schedule-mail-task-changed}")
    private String scheduleMailChanged;

    @Value("${magreport.schedule-mail-task-user-error}")
    private String scheduleMailErrorUser;

    private static final String LINE_BREAK = "<br/>";


    public void sendScheduleMailExcel(String code, ScheduleTaskResponse taskResponse, List<Pair<String, File>> attachments, Pair<String, String> warning) {

        var mailText = repository.findByCode(code);
        var warningText = "";
        if (!warning.getL().isEmpty())
            warningText = updateTextForScheduleService(repository.findByCode(warning.getL()).getBody(), taskResponse, warning.getR(), "");

        emailService.sendMailToList(
                Message.RecipientType.TO,
                getEmails(DestinationTypeEnum.REPORT, taskResponse.getDestinationEmails(), taskResponse.getDestinationUsers(), taskResponse.getDestinationRoles()),
                updateTextForScheduleService(taskResponse.getReportTitleMail() == null ? mailText.getSubject() : taskResponse.getReportTitleMail(), taskResponse),
                updateTextForScheduleService(taskResponse.getReportBodyMail(), taskResponse) + LINE_BREAK + warningText,
                attachments
        );
    }

    public void sendScheduleMailBigExcel(String code, ScheduleTaskResponse taskResponse, Long reportJobId, String linkService, Pair<String, String> warning) {

        var mailText = repository.findByCode(code);
        var warningText = !warning.getL().isEmpty() ? repository.findByCode(warning.getL()).getBody() : "";
        var emails = getEmails(DestinationTypeEnum.REPORT, taskResponse.getDestinationEmails(), taskResponse.getDestinationUsers(), taskResponse.getDestinationRoles());

        emails.forEach(email -> {
            String token = jobTokenDomainService.createJobToken(reportJobId, taskResponse.getExcelTemplate().getId(), email);
            String fileLink = linkService.replace("{reportToken}", token);

            emailService.sendMail(
                    Message.RecipientType.TO,
                    email,
                    updateTextForScheduleService(taskResponse.getReportTitleMail() == null ? mailText.getSubject() : taskResponse.getReportTitleMail(), taskResponse),
                    updateTextForScheduleService(taskResponse.getReportBodyMail() + LINE_BREAK + mailText.getBody() + LINE_BREAK + warningText, taskResponse, warning.getR(), fileLink, reportJobId),
                    new ArrayList<>()
            );
        });
    }

    public void sendScheduleMailWeb(String code, ScheduleTaskResponse taskResponse, Long reportJobId, Pair<String, String> warning) {

        var mailText = repository.findByCode(code);

        var warningText = "";
        if (!warning.getL().isEmpty())
            warningText = updateTextForScheduleService(repository.findByCode(warning.getL()).getBody(), taskResponse, warning.getR(), "", reportJobId);


        emailService.sendMailToList(
                Message.RecipientType.TO,
                getEmails(DestinationTypeEnum.REPORT, taskResponse.getDestinationEmails(), taskResponse.getDestinationUsers(), taskResponse.getDestinationRoles()),
                updateTextForScheduleService(taskResponse.getReportTitleMail() == null ? mailText.getSubject() : taskResponse.getReportTitleMail(), taskResponse, warning.getR().isEmpty() ? "" : warning.getR(), "", reportJobId),
                updateTextForScheduleService(mailText.getBody(), taskResponse, warning.getR().isEmpty() ? "" : warning.getR(), "", reportJobId) + LINE_BREAK + warningText,
                new ArrayList<>()
        );
    }

    public void sendScheduleMailExpired(String code, ScheduleTaskResponse taskResponse, String link) {

        var mailText = repository.findByCode(code);

        emailService.sendMailToList(
                Message.RecipientType.TO,
                getEmails(DestinationTypeEnum.REPORT, taskResponse.getDestinationEmails(), taskResponse.getDestinationUsers(), taskResponse.getDestinationRoles()),
                updateTextForScheduleService(mailText.getSubject(), taskResponse),
                updateTextForScheduleService(mailText.getBody(), taskResponse, link, ""),
                new ArrayList<>()
        );
    }

    public void sendScheduleMailChanged(ScheduleTaskResponse taskResponse) {

        var mailText = repository.findByCode(scheduleMailChanged);

        emailService.sendMail(
                Message.RecipientType.TO,
                adminMailBox,
                updateTextForScheduleService(mailText.getSubject(), taskResponse),
                updateTextForScheduleService(mailText.getBody(), taskResponse),
                new ArrayList<>()
        );

    }

    public void sendScheduleMailFailed(String code, ScheduleTaskResponse taskResponse, Long reportJobId, Pair<String, StackTraceElement[]> error) {

        var mailText = repository.findByCode(code);
        var userMailText = repository.findByCode(scheduleMailErrorUser);

        emailService.sendMail(
                Message.RecipientType.TO,
                adminMailBox,
                updateTextForScheduleService(mailText.getSubject(), taskResponse, reportJobId, error),
                updateTextForScheduleService(mailText.getBody(), taskResponse, reportJobId, error),
                new ArrayList<>()
        );

        var emails = getEmails(DestinationTypeEnum.ERROR, taskResponse.getDestinationEmails(), taskResponse.getDestinationUsers(), taskResponse.getDestinationRoles());

        if (!emails.isEmpty()) {
            emailService.sendMailToList(
                    Message.RecipientType.TO,
                    emails,
                    updateTextForScheduleService(taskResponse.getErrorTitleMail() == null ? userMailText.getSubject() : taskResponse.getErrorTitleMail(), taskResponse),
                    updateTextForScheduleService(taskResponse.getErrorBodyMail() == null ? userMailText.getBody() : taskResponse.getErrorBodyMail(), taskResponse),
                    new ArrayList<>()
            );
        }

    }

    public void sendScheduleMailDeadline(String code, ScheduleTaskResponse taskResponse, String prolongationLink) {
        var mailText = repository.findByCode(code);

        emailService.sendMailToList(
                Message.RecipientType.TO,
                getEmails(DestinationTypeEnum.REPORT, taskResponse.getDestinationEmails(), taskResponse.getDestinationUsers(), taskResponse.getDestinationRoles()),
                updateTextForScheduleService(mailText.getSubject(), taskResponse),
                updateTextForScheduleService(mailText.getBody(), taskResponse, prolongationLink, ""),
                new ArrayList<>()
        );
    }

    private List<String> getEmails(
            DestinationTypeEnum destinationType,
            List<DestinationEmailResponse> destinationEmails,
            List<DestinationUserResponse> destinationUsers,
            List<DestinationRoleResponse> destinationRoles) {

        var result = destinationEmails.stream()
                .filter(d -> d.getType().equals(destinationType))
                .map(DestinationEmailResponse::getValue)
                .collect(Collectors.toList());

        result.addAll(destinationRoles.stream()
                .filter(d -> d.getType().equals(destinationType))
                .map(t -> roleService.getRoleUsers(new RoleRequest().setId(t.getRoleId())))
                .flatMap(user -> user.getUsers().stream())
                .map(UserResponse::getEmail)
                .toList());

        result.addAll(destinationUsers.stream()
                .filter(d -> d.getType().equals(destinationType))
                .map(user -> userService.getUserResponse(new UserRequest().setUserName(user.getUserName())).getEmail())
                .toList());

        return result;
    }


    private String updateTextForScheduleService(String updateText, ScheduleTaskResponse taskResponse) {
        return updateTextForScheduleService(updateText, taskResponse, "", "", 0L, null);
    }

    private String updateTextForScheduleService(String updateText, ScheduleTaskResponse taskResponse, String prolongationLink, String fileLink) {
        return updateTextForScheduleService(updateText, taskResponse, prolongationLink, fileLink, 0L, null);
    }

    private String updateTextForScheduleService(String updateText, ScheduleTaskResponse taskResponse, String prolongationLink, String fileLink, Long reportJob) {
        return updateTextForScheduleService(updateText, taskResponse, prolongationLink, fileLink, reportJob, null);
    }

    private String updateTextForScheduleService(String updateText, ScheduleTaskResponse taskResponse, Long reportJob, Pair<String, StackTraceElement[]> ex) {
        return updateTextForScheduleService(updateText, taskResponse, "", "", reportJob, ex);
    }

    private String updateTextForScheduleService(String updateText, ScheduleTaskResponse taskResponse, String prolongationLink, String fileLink, Long reportJobId, Pair<String, StackTraceElement[]> ex) {

        if (updateText == null) {
            return "Ошибка! Текст не найден";
        }

        updateText = updateTextForError(updateText, ex);
        updateText = updateTextForSystemValue(updateText);

        return updateText
                .replace("{reportName}", taskResponse.getReport().getName())
                .replace("{reportId}", taskResponse.getReport().getId().toString())
                .replace("{reportJobId}", reportJobId.toString())
                .replace("{taskId}", taskResponse.getId().toString())
                .replace("{taskName}", taskResponse.getName())
                .replace("{prolongationLink}", prolongationLink)
                .replace("{taskDescription}", taskResponse.getDescription())
                .replace("{expiredDate}", taskResponse.getExpirationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .replace("{fileLink}", fileLink);
    }

    private String updateTextForError(String updateText, Pair<String, StackTraceElement[]> ex) {
        if (ex == null)
            ex = new Pair<>();

        return updateText
                .replace("{textError}", ex.getL() == null ? "-" : ex.getL())
                .replace("{stackTrace}", ex.getR() == null ? "-" : Arrays.stream(ex.getR()).map(StackTraceElement::toString).collect(Collectors.joining("</br>")));
    }

    private String updateTextForSystemValue(String updateText) {
        return updateText.replace("{currentDataTime}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
    }

}
