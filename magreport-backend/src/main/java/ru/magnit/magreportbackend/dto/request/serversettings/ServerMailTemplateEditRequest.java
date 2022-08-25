package ru.magnit.magreportbackend.dto.request.serversettings;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class ServerMailTemplateEditRequest {

    Long id;
    String name;
    String description;
    String subject;
    String body;

}
