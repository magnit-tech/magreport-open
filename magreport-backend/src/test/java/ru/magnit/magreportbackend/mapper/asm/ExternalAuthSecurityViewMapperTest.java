package ru.magnit.magreportbackend.mapper.asm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.asm.ExternalAuth;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSource;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceTypeEnum;
import ru.magnit.magreportbackend.domain.user.RoleType;
import ru.magnit.magreportbackend.domain.user.RoleTypeEnum;
import ru.magnit.magreportbackend.dto.inner.asm.ExternalAuthSourceView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalAuthSecurityViewMapperTest {

    private static final Long ID = 1L;
    private static final RoleTypeEnum ROLE_TYPE_ENUM = RoleTypeEnum.SYSTEM;
    private static final List<ExternalAuthSource> SOURCES = Collections.singletonList(new ExternalAuthSource());

    @Mock
    private ExternalAuthSourceViewMapper sourceViewMapper;

    @InjectMocks
    private ExternalAuthSecurityViewMapper mapper;

    @Test
    void from() {
        Map<ExternalAuthSourceTypeEnum, ExternalAuthSourceView> sourceMap = new HashMap<>();

        final var source = spy(getExternalAuth());

        sourceMap.put(ExternalAuthSourceTypeEnum.PERMISSION_SOURCE, new ExternalAuthSourceView());

        when(sourceViewMapper.mapFrom(anyList())).thenReturn(sourceMap);

        final var result = mapper.from(source);

        assertEquals(ID, result.getId());
        assertNotNull(result.getRoleType());
        assertNotNull(result.getSources());
        assertEquals(SOURCES.size(), result.getSources().size());

        verify(source).getId();
        verify(source).getRoleType();
        verify(source).getSources();


        verifyNoMoreInteractions(source, sourceViewMapper);
    }

    private ExternalAuth getExternalAuth() {
        return new ExternalAuth()
                .setId(ID)
                .setSources(SOURCES)
                .setRoleType(new RoleType(ROLE_TYPE_ENUM));
    }
}