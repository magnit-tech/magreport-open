package ru.magnit.magreportbackend.mapper.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.user.RoleTypeEnum;
import ru.magnit.magreportbackend.dto.request.user.RoleAddRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class RoleMapperTest {

    public static final String NAME = "Test role";
    public static final String DESCRIPTION = "Test role description";
    public static final long TYPE_ID = RoleTypeEnum.SYSTEM.ordinal();

    @InjectMocks
    private RoleMapper mapper;

    @Test
    void from() {
        var result = mapper.from(getRoleAddRequest());

        assertNull(result.getId());
        assertEquals(NAME, result.getName());
        assertEquals(DESCRIPTION, result.getDescription());
        assertEquals(TYPE_ID, result.getRoleType().getId());
    }

    private RoleAddRequest getRoleAddRequest() {

        return new RoleAddRequest()
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setTypeId(TYPE_ID);
    }
}