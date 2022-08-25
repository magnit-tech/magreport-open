package ru.magnit.magreportbackend.dto.response.reportjob;

import com.fasterxml.jackson.annotation.JsonFormat;

public record TokenResponse(

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String token
) {
}
