package ru.magnit.magreportbackend.mapper.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import ru.magnit.magreportbackend.dto.inner.RoleView;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class GrantedAuthorityMapperTest {

    private final Long ID = 1L;
    private final String NAME = "Test folder";
    private final String DESCRIPTION = "Folder description";

    @InjectMocks
    private GrantedAuthorityMapper mapper;

    @Test
    void from() {
        GrantedAuthority authority = mapper.from(getRoleView());
        assertNotNull(authority);

        List<GrantedAuthority> authorities = mapper.from(Collections.singletonList(getRoleView()));
        assertNotEquals(0, authorities.size());
        authority = authorities.get(0);

        assertNotNull(authority);
    }

    private RoleView getRoleView() {
        return new RoleView()
            .setId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION);
    }
}