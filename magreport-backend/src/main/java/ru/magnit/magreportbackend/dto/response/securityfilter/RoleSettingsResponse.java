package ru.magnit.magreportbackend.dto.response.securityfilter;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;
import ru.magnit.magreportbackend.dto.tuple.Tuple;

import java.util.List;

public record RoleSettingsResponse(

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        RoleResponse role,

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<Tuple> tuples
) {
}
