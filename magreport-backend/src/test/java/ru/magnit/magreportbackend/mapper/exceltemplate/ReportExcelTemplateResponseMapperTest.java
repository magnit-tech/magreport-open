package ru.magnit.magreportbackend.mapper.exceltemplate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplate;
import ru.magnit.magreportbackend.domain.excel.ReportExcelTemplate;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ReportExcelTemplateResponseMapperTest {

    private final static Long ID = 1L;
    private final static String NAME = "Name";
    private final static String DESCRIPTION = "Description";
    private final static Boolean DEFAULT = false;

    @InjectMocks
    private ReportExcelTemplateResponseMapper mapper;

    @Test
    void from() {

        var response = mapper.from(getReportExcelTemplate());

        assertEquals(ID, response.getExcelTemplateId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(DEFAULT, response.getIsDefault());

    }

    @Test
    void fromList() {

        var responses = mapper.from(Collections.singletonList(getReportExcelTemplate()));

        assertEquals(1, responses.size());
        var response = responses.get(0);

        assertEquals(ID, response.getExcelTemplateId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(DEFAULT, response.getIsDefault());
    }


    private ReportExcelTemplate getReportExcelTemplate() {
        return new ReportExcelTemplate()
                .setId(ID)
                .setExcelTemplate(
                        new ExcelTemplate()
                                .setId(ID)
                                .setName(NAME)
                                .setDescription(DESCRIPTION))
                .setIsDefault(DEFAULT);
    }
}
