package ru.magnit.magreportbackend.mapper.asm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.dto.request.asm.AsmSecurityFilterInstanceFieldAddRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExternalAuthSourceFieldFilterInstanceFieldMapperTest {

    private static final Long ID = 1L;
    private static final Long FILTER_INSTANCE_FIELD_ID = 3L;

    @InjectMocks
    private ExternalAuthSourceFieldFilterInstanceFieldMapper mapper;

    @Test
    void from() {

        final var source = spy(getAsmSecurityFilterInstanceFieldAddRequest());
        final var result = mapper.from(source);

        assertEquals(ID, result.getId());
        assertEquals(FILTER_INSTANCE_FIELD_ID, result.getFilterInstanceField().getId());
        assertNull(result.getSourceField());

        verify(source).getId();
        verify(source).getFilterInstanceFieldId();
        verifyNoMoreInteractions(source);
    }

    private AsmSecurityFilterInstanceFieldAddRequest getAsmSecurityFilterInstanceFieldAddRequest() {
        return new AsmSecurityFilterInstanceFieldAddRequest()
                .setId(ID)
                .setFilterInstanceFieldId(FILTER_INSTANCE_FIELD_ID);
    }
}