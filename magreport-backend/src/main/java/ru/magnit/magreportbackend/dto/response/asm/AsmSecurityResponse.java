package ru.magnit.magreportbackend.dto.response.asm;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.dto.response.user.RoleTypeResponse;

import java.time.LocalDateTime;
import java.util.List;

import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

public record AsmSecurityResponse(

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Long id,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String name,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String description,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<AsmSecuritySourceResponse> sources,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String userName,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        RoleTypeResponse roleType,

        @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
        LocalDateTime created,

        @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
        LocalDateTime modified
) {
}