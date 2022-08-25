package ru.magnit.magreportbackend.dto.request.asm;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AsmSecurityFilterInstanceFieldAddRequest {

    private Long id;
    private Long filterInstanceFieldId;
}
