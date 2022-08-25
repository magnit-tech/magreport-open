package ru.magnit.magreportbackend.dto.response.asm;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterResponse;

import java.time.LocalDateTime;

import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

public record AsmSecurityFilterResponse(

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Long id,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        SecurityFilterResponse securityFilter,

        @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
        LocalDateTime created,

        @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
        LocalDateTime modified
) {}