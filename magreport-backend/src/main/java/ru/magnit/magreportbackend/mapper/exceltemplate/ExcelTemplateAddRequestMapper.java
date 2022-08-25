package ru.magnit.magreportbackend.mapper.exceltemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.dto.request.exceltemplate.ExcelTemplateAddRequest;
import ru.magnit.magreportbackend.exception.JsonSerializationException;
import ru.magnit.magreportbackend.mapper.Mapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelTemplateAddRequestMapper implements Mapper<ExcelTemplateAddRequest, String> {

    @Override
    public ExcelTemplateAddRequest from(String source) {
        return mapBaseProperties(source);
    }

    private ExcelTemplateAddRequest mapBaseProperties(String source) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(source, ExcelTemplateAddRequest.class);
        } catch (Exception ex) {
            log.error("Error trying to deserialize AddExcelTemplateRequest.");
            throw new JsonSerializationException("Error trying to deserialize ExcelTemplateAddRequest.", ex);
        }
    }
}
