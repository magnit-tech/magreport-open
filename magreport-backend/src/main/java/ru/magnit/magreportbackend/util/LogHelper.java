package ru.magnit.magreportbackend.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.magnit.magreportbackend.dto.inner.olap.OlapUserRequestLog;

@Slf4j
@UtilityClass
public class LogHelper {

    private final String IN_MESSAGE = "IN: ";
    private final String OUT_MESSAGE = "OUT: ";
    private final String INFO_MESSAGE = "INFO: ";

    private static final Logger olapRequestLog = LoggerFactory.getLogger("ru.magnit.magreportbackend.olap_user_request_logger");
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void logDebugMethodStart() {
        if (!log.isDebugEnabled()) return;
        log.debug(IN_MESSAGE + getCurrentMethodName());
    }

    public void logDebugMethodEnd() {
        if (!log.isDebugEnabled()) return;
        log.debug(OUT_MESSAGE + getCurrentMethodName());
    }

    public void logDebugMessage(String message) {
        if (!log.isDebugEnabled()) return;
        log.debug(INFO_MESSAGE + getCurrentMethodName() + " - " + message);
    }

    public void logInfoUserMethodStart() {
        if (!log.isInfoEnabled()) return;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info(IN_MESSAGE + getCurrentMethodName() + ", user: " + authentication.getName());
    }


    public void logInfoUserMethodEnd() {
        if (!log.isInfoEnabled()) return;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info(OUT_MESSAGE + getCurrentMethodName() + ", user: " + authentication.getName());
    }

    public void logInfoOlapUserRequest(OlapUserRequestLog request) throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        request.setUser(authentication.getName());
        var message = objectMapper.writeValueAsString(request);
        olapRequestLog.debug(message);

    }

    private String getCurrentMethodName() {
        return new Throwable().getStackTrace()[2].getMethodName();
    }
}
