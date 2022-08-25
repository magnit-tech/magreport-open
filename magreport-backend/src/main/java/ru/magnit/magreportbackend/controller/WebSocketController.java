package ru.magnit.magreportbackend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Slf4j
@Controller
@RequiredArgsConstructor
@Tag(name = "Контроллер веб-сокета")
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void onSessionConnectedEvent(SessionConnectedEvent event){

        var sessionId = event.getMessage().getHeaders().get("simpSessionId");
        var username =  event.getUser() != null ? event.getUser().getName() : "unknown user";
        log.debug("New connected to socket: " + username + ":" + sessionId.toString());
    }

}
