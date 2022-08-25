package ru.magnit.magreportbackend.dto.response.serversettings;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class ServerSettingsFolderResponse {

    private int ordinal;

    private String code;

    private String name;

    private String description;

    private List<ServerParameterResponse> parameters = Collections.emptyList();
}
