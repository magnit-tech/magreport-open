package ru.magnit.magreportbackend.dto.request.asm;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AsmSecuritySecurityFilterAddRequest {

    private Long id;
    private Long securityFilterId;
}
