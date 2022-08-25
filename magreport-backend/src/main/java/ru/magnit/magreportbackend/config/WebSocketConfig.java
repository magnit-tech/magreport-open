package ru.magnit.magreportbackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import ru.magnit.magreportbackend.service.security.AuthChannelInterceptor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final AuthChannelInterceptor channelInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
          registry
                  .setApplicationDestinationPrefixes("/app")
                  .setUserDestinationPrefix("/user")
                  .enableSimpleBroker("/report");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry ){

        var strategy = new TomcatRequestUpgradeStrategy();

        registry.addEndpoint("/stomp")
                .setHandshakeHandler(new DefaultHandshakeHandler(strategy))
                .setAllowedOriginPatterns("*");

        registry.addEndpoint("/stomp")
                .setHandshakeHandler(new DefaultHandshakeHandler(strategy))
                .setAllowedOriginPatterns("*").withSockJS();

    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(channelInterceptor);
    }


}
