package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.dto.response.reportjob.ReportJobResponse;

@Service
@RequiredArgsConstructor
public class StompMessageService {

    private final SimpMessagingTemplate webSocketMessageSender;

    @Value("${magreport.stomp.report-status}")
    private String reportStatus;


    public void sendReportStatus(String user, ReportJobResponse data){
        sendPrivateStompMessage(user,reportStatus, data);
    }

    private void sendSharedStompMessage(String path, Object data){
        webSocketMessageSender.convertAndSend(path,data);
    }


    private void sendPrivateStompMessage(String username, String path, Object data ){
        webSocketMessageSender.convertAndSendToUser(username,path,data);
    }





}
