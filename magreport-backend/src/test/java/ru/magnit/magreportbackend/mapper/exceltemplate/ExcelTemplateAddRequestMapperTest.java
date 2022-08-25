package ru.magnit.magreportbackend.mapper.exceltemplate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.dto.request.exceltemplate.ExcelTemplateAddRequest;
import ru.magnit.magreportbackend.exception.JsonSerializationException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ExcelTemplateAddRequestMapperTest {

    private final Long ID = 1L;
    private final String NAME = "Test folder";
    private final String DESCRIPTION = "Folder description";

    @InjectMocks
    private ExcelTemplateAddRequestMapper mapper;

    @Test
    void from() {
        ExcelTemplateAddRequest request = mapper.from(getSource());

        assertEquals(request.getFolderId(), ID);
        assertEquals(request.getName(), NAME);
        assertEquals(request.getDescription(), DESCRIPTION);

        List<ExcelTemplateAddRequest> requests = mapper.from(Collections.singletonList(getSource()));
        assertNotEquals(0, requests.size());

        request = requests.get(0);

        assertEquals(request.getFolderId(), ID);
        assertEquals(request.getName(), NAME);
        assertEquals(request.getDescription(), DESCRIPTION);

        assertThrows(JsonSerializationException.class, ()-> mapper.from(""));
    }



    private String getSource() {
        return "{" +
            "\"folderId\":" + ID + ", " +
            "\"name\":\"" + NAME + "\"," +
            " \"description\":\"" + DESCRIPTION+"\"" +
            "}";
    }
}