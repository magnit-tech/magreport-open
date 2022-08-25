package ru.magnit.magreportbackend.mapper.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.domain.user.RoleType;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
class RoleResponseMapperTest {

    private static final long ID = 1L;
    private static final String NAME = "Test role";
    private static final String DESCRIPTION = "Test role description";
    private static final long TYPE_ID = 2L;

    @InjectMocks
    private RoleResponseMapper mapper;

    @Test
    void from() {

        final var result = mapper.from(getRole());

        assertEquals(ID, result.getId());
        assertEquals(NAME, result.getName());
        assertEquals(DESCRIPTION, result.getDescription());
        assertEquals(TYPE_ID, result.getTypeId());
    }

    private Role getRole() {

        return new Role()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setRoleType(new RoleType(TYPE_ID));
    }
}