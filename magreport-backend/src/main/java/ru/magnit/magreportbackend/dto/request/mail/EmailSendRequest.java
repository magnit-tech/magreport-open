package ru.magnit.magreportbackend.dto.request.mail;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)

public class EmailSendRequest {

    private String subject;
    private String body;
    private List<UserResponse> to = Collections.emptyList();
    private List<UserResponse> cc = Collections.emptyList();
    private List<UserResponse> bcc = Collections.emptyList();

    public boolean checkItem(){
        return subject.length() > 0 && body.length() > 0 && !(to.isEmpty() && cc.isEmpty() && bcc.isEmpty());
    }

}
