package ru.magnit.magreportbackend.dto.response.serversettings;

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
public class ServerParameterResponse {

    private long id;

    private int ordinal;

    private boolean encoded;

    private String code;

    private String name;

    private String description;

    private String value;
}
