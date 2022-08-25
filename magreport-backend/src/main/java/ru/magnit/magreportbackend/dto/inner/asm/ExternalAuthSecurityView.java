package ru.magnit.magreportbackend.dto.inner.asm;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceTypeEnum;
import ru.magnit.magreportbackend.domain.user.RoleTypeEnum;

import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class ExternalAuthSecurityView {

    private Long id;
    private RoleTypeEnum roleType;
    private Map<ExternalAuthSourceTypeEnum, ExternalAuthSourceView> sources;
}
