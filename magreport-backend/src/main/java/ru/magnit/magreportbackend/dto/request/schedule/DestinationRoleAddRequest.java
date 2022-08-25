package ru.magnit.magreportbackend.dto.request.schedule;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
public class DestinationRoleAddRequest {
    Long roleId;
    Long typeId;
    String name;
}
