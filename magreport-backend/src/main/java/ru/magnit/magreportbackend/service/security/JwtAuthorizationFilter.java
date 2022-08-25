package ru.magnit.magreportbackend.service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.user.UserStatusEnum;
import ru.magnit.magreportbackend.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ru.magnit.magreportbackend.domain.user.UserStatusEnum.LOGGED_OFF;


@Slf4j
@Service
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final UserService userService;

    @Value("${jwt.properties.headerString}")
    private String headerString;
    @Value("${jwt.properties.tokenPrefix}")
    private String tokenPrefix;
    @Value("${jwt.properties.secretKey}")
    private String secretKey;

    public JwtAuthorizationFilter(@Lazy AuthenticationManager authenticationManager, @Lazy UserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader(headerString);

        if (token == null || !token.startsWith(tokenPrefix)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(token);

        if (isLoggedOff(response, authenticationToken)) return;

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        chain.doFilter(request, response);
    }

    private boolean isLoggedOff(HttpServletResponse response, UsernamePasswordAuthenticationToken authenticationToken) {
        if (authenticationToken == null) return false;

        String userName = authenticationToken.getName();
        UserStatusEnum userStatus = userService.getUserStatus(userName);

        if (userStatus == null || userStatus == LOGGED_OFF) {
            form401Message(response);
            return true;
        }

        return false;
    }

    private void form401Message(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try {
            response.getOutputStream().println("{\"success\": false,\"message\": \"Forced logoff\",\"data\":{\"status\":401}}");
        } catch (IOException ex) {
            log.error("Error while trying to write response body:\n" + ex.getMessage(), ex);
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(secretKey))
            .build()
            .verify(token.replace(tokenPrefix, "").trim());

        if (decodedJWT == null) return null;

        String userName = decodedJWT.getSubject();

        List<GrantedAuthority> roles = userService.getUserAuthorities(userName);

        return new UsernamePasswordAuthenticationToken(userName, null, roles);
    }
}
