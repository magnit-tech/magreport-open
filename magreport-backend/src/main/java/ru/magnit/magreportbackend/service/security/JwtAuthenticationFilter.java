package ru.magnit.magreportbackend.service.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.dto.request.user.LoginRequest;
import ru.magnit.magreportbackend.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedList;
import java.util.stream.Collectors;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Slf4j
@Service
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Value("${jwt.properties.validityDuration}")
    private Long validityDuration;
    @Value("${jwt.properties.headerString}")
    private String headerString;
    @Value("${jwt.properties.tokenPrefix}")
    private String tokenPrefix;
    @Value("${jwt.properties.secretKey}")
    private String secretKey;

    public JwtAuthenticationFilter(@Lazy AuthenticationManager authenticationManager, @Lazy UserService userService) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            LoginRequest credentials = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
            credentials.setUserName(credentials.getUserName().toLowerCase());

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                credentials.getUserName(),
                credentials.getPassword(),
                new LinkedList<>()
            ));
        } catch (Exception ex) {
            log.error("Error while attempting authenticate user: " + ex.getMessage());
            throw new AuthenticationException(ex.getMessage(), ex) {
            };
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult){
       var user = userService.loginUser(authResult.getName(), authResult.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        if(user != null) {
            String token = JWT.create()
                    .withSubject(authResult.getName())
                    .withExpiresAt(new Date(System.currentTimeMillis() + validityDuration))
                    .withClaim("Roles", userService.getUserRoles(authResult.getName()))
                    .sign(HMAC512(secretKey.getBytes()));

            response.addHeader(headerString, tokenPrefix + " " + token);
            response.addHeader("Access-Control-Expose-Headers", headerString);
        }

        else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        }
    }
}
