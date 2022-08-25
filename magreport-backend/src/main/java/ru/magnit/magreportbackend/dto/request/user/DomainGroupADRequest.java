package ru.magnit.magreportbackend.dto.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Запрос доменных групп из AD по подстроке")
public class DomainGroupADRequest {

    private String namePart;
    private Long maxResults;
}
