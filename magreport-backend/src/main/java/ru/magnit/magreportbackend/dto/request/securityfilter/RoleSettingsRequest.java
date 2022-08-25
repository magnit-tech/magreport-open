package ru.magnit.magreportbackend.dto.request.securityfilter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.dto.tuple.Tuple;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class RoleSettingsRequest {

    private Long roleId;

    private List<Tuple> tuples = Collections.emptyList();
}
