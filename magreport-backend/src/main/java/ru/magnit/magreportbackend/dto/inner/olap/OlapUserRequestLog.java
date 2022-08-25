package ru.magnit.magreportbackend.dto.inner.olap;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OlapUserRequestLog {

    private String dateTime = LocalDateTime.now().toString();
    private String url;
    private String user;
    private Object request;


    public OlapUserRequestLog(String url, Object request) {
        this.url = url;
        this.request = request;
    }
}
