package ru.magnit.magreportbackend.dto.response.serversettings;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class ServerSettingsJournalResponse {

    private long id;

    private String code;

    private String name;

    private String description;

    private String user;

    private String valueBefore;

    private String valueAfter;

    private LocalDateTime changeDate;
}
