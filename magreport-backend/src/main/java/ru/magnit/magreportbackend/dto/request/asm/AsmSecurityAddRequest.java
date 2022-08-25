package ru.magnit.magreportbackend.dto.request.asm;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Accessors(chain = true)
public class AsmSecurityAddRequest {

    private Long id;
    private String name;
    private String description;
    private Long roleTypeId;
    private List<AsmSecuritySourceAddRequest> securitySources = Collections.emptyList();
}
