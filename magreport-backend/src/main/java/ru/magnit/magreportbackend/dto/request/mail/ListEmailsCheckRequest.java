package ru.magnit.magreportbackend.dto.request.mail;

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

public class ListEmailsCheckRequest {

    List<String> emails = Collections.emptyList();

}
