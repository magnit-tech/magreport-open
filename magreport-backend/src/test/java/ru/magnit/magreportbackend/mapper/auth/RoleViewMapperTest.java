package ru.magnit.magreportbackend.mapper.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.dto.inner.RoleView;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
class RoleViewMapperTest {

    private final Long ID = 1L;
    private final String NAME = "Test folder";
    private final String DESCRIPTION = "Folder description";

    @InjectMocks
    private RoleViewMapper mapper;

    @Test
    void from() {
        RoleView view = mapper.from(getRole());

        assertEquals(ID, view.getId());
        assertEquals(NAME, view.getName());
        assertEquals(DESCRIPTION, view.getDescription());

        List<RoleView> views = mapper.from(Collections.singletonList(getRole()));
        assertNotEquals(0, views.size());
        view = views.get(0);

        assertEquals(ID, view.getId());
        assertEquals(NAME, view.getName());
        assertEquals(DESCRIPTION, view.getDescription());
    }

    private Role getRole() {
        return new Role()
            .setId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION);
    }
}