package ru.magnit.magreportbackend.service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.service.UserService;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthChannelInterceptor implements ChannelInterceptor {

    private final UserService userService;
    private final Environment environment;
    private static final String JWT_TOKEN = "jwtToken";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        assert accessor != null;
        if (accessor.getCommand() == StompCommand.CONNECT) {
            final var token = accessor.getFirstNativeHeader(JWT_TOKEN);
            if (token != null) {
                UsernamePasswordAuthenticationToken user = getTokenAuthentication(token);
                accessor.setUser(user);
            }
        }

        return message;

    }

    UsernamePasswordAuthenticationToken getTokenAuthentication(String token) {

        var tokenPrefix = Objects.requireNonNull(environment.getProperty("jwt.properties.tokenPrefix"));
        var secretKey = Objects.requireNonNull(environment.getProperty("jwt.properties.secretKey"));

        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(secretKey))
                .build()
                .verify(token.replace(tokenPrefix, "").trim());

        if (decodedJWT == null) return null;

        var userName = decodedJWT.getSubject();

        List<GrantedAuthority> roles = userService.getUserAuthorities(userName);

        return new UsernamePasswordAuthenticationToken(userName, null, roles);

    }


}
