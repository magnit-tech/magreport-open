package ru.magnit.magreportbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.config.MailSenderFactory;
import ru.magnit.magreportbackend.dto.request.mail.EmailSendRequest;
import ru.magnit.magreportbackend.dto.request.mail.ListEmailsCheckRequest;
import ru.magnit.magreportbackend.dto.response.mail.ListEmailsCheckResponse;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;
import ru.magnit.magreportbackend.exception.InvalidApplicationSettings;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.util.Pair;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final MailSenderFactory mailSenderFactory;
    private final SettingsService settingsService;

    @Value("${mailAddressFrom}")
    String fromCode;

    @Value("${magreport.mail.permitted-domains}")
    String permittedDomainsCode;

    @Value("${magreport.mail.send-emails}")
    boolean sendEmails;

    @Value("${magreport.mail.tag-subject}")
    String tag;

    private static final String ERROR_MESSAGE = "Error to send email";

    public void sendMail(Message.RecipientType type, String emailTo, String subject, String textMessage, List<File> attachments) {

        sendMailToList(type, Collections.singletonList(emailTo), subject, textMessage, attachments.stream().map(attachment -> new Pair<>(attachment.getName(),attachment)).toList());
    }

    public void sendMailToList(Message.RecipientType type, List<String> emailTo, String subject, String textMessage, List<Pair<String,File>> attachments) {

        if(!sendEmails){
            log.warn("The ability to send messages is disabled");
            return;
        }

        if (emailTo.isEmpty()){
            log.warn("Destinations list is empty. Mail not send.");
            return;
        }

        var mailSender = getMailSender();

        var msg = mailSender.createMimeMessage();
        var from = settingsService.getValueSetting(fromCode);


        try {
            var mimeMessage = new MimeMessageHelper(msg, true, "UTF-8");
            mimeMessage.setFrom(from);
            mimeMessage.setSubject(String.format("%s %s",tag,subject));
            mimeMessage.setText(textMessage, true);

            if (type.equals(Message.RecipientType.BCC))
                mimeMessage.setBcc(checkAndFilterEmails(emailTo));
            else
                mimeMessage.setTo(checkAndFilterEmails(emailTo));

            attachments.forEach(attachment -> {
                try {
                    mimeMessage.addAttachment(attachment.getL(), attachment.getR());
                } catch (MessagingException e) {
                    log.error(ERROR_MESSAGE, e);
                }
            });
                 mailSender.send(msg);
        } catch (MessagingException e) {
            log.error(ERROR_MESSAGE, e);
            throw new InvalidApplicationSettings("", e);
        }
    }

    public void sendMail(EmailSendRequest request) {

        if(!sendEmails){
            log.info("The ability to send messages is disabled");
            return;
        }

        if (request.checkItem()) {
            var mailSender = getMailSender();

            var msg = mailSender.createMimeMessage();
            var from = settingsService.getValueSetting(fromCode);

            var to = request.getTo().stream().map(UserResponse::getEmail).toList();
            var cc = request.getCc().stream().map(UserResponse::getEmail).toList();
            var bcc = request.getBcc().stream().map(UserResponse::getEmail).toList();

            try {
                var mimeMessage = new MimeMessageHelper(msg, true, "UTF-8");
                mimeMessage.setFrom(from);
                mimeMessage.setSubject(String.format("%s %s",tag,request.getSubject()));
                mimeMessage.setText(request.getBody(), true);

                mimeMessage.setTo(checkAndFilterEmails(to));
                mimeMessage.setCc(checkAndFilterEmails(cc));
                mimeMessage.setBcc(checkAndFilterEmails(bcc));

                mailSender.send(msg);
            } catch (MessagingException e) {
                log.error(ERROR_MESSAGE, e);
            }
        } else {
            throw new InvalidParametersException("Request contains empty fields");
        }
    }

    public ListEmailsCheckResponse checkEmails(ListEmailsCheckRequest request) {
        var emails = new ArrayList<>(request.getEmails().stream().map(String::toLowerCase).toList());
        var goodEmails = checkAndFilterEmails(request.getEmails());
        emails.removeAll(Arrays.stream(goodEmails).toList());
        return new ListEmailsCheckResponse().setEmails(emails);

    }


    private JavaMailSender getMailSender() {
        try {
            return mailSenderFactory.getJavaMailSender();
        } catch (Exception ex) {
            throw new InvalidApplicationSettings("Error create mail sender", ex);
        }
    }

    private String[] checkAndFilterEmails(List<String> emails) {

        var badEmail = emails
                .stream()
                .filter(s -> !s.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
                .distinct()
                .toList();

        if (!badEmail.isEmpty()) {
            log.error("These email addresses are invalid: " + badEmail);
            emails.removeAll(badEmail);
        }


        var permittedDomains = Arrays.stream(
                settingsService
                        .getValueSetting(permittedDomainsCode)
                        .replace(" ", "")
                        .toLowerCase()
                        .split(",")
        )
                .toList();

        if (!permittedDomains.get(0).isBlank())
            emails = emails
                    .stream()
                    .map(String::toLowerCase)
                    .filter(email -> permittedDomains.stream().anyMatch(email::contains))
                    .toList();


        return emails.stream().distinct().toList().toArray(new String[0]);
    }
}
