package ru.magnit.magreportbackend.dto.response.securityfilter;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

public record SecurityFilterRoleSettingsResponse(

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Long securityFilterId,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<RoleSettingsResponse> roleSettings
){}