package ru.magnit.magreportbackend.dto.response.securityfilter;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

public record SecurityFilterValuesCheckResponse (

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    SecurityFilterResponse filter,

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    List<String> values ) {}
