package ru.magnit.magreportbackend.service.domain;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.PlainTextOutputFormat;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.exception.TemplateParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.magnit.magreportbackend.util.Constant.NEWLINE;

@Service
@RequiredArgsConstructor
public class TemplateParserService {

    public String parseTemplate(String templatePath, Object data) {

        var template = getTemplateText(templatePath);

        return parse(template, data);
    }

    private String parse(String template, Object data) {

        try (Writer writer = new StringWriter()) {
            Template fmTemplate = getTemplate(template);
            fmTemplate.process(data, writer);

            return writer.toString().replace("/n", "\n");
        } catch (IOException | TemplateException ex) {
            throw new TemplateParseException(ex.getMessage(), ex);
        }
    }

    private Template getTemplate(String template) throws IOException {

        Configuration configuration = getConfiguration(template);

        return configuration.getTemplate("template");
    }

    private Configuration getConfiguration(String template) {

        Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        StringTemplateLoader templateLoader = new StringTemplateLoader();
        templateLoader.putTemplate("template", template);
        configuration.setOutputFormat(PlainTextOutputFormat.INSTANCE);
        configuration.setTemplateLoader(templateLoader);
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setObjectWrapper(new BeansWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));
        configuration.setWhitespaceStripping(true);

        return configuration;
    }

    private String getTemplateText(String templatePath) {

        try (InputStream is = this.getClass().getResourceAsStream(templatePath);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(is)))) {
            return bufferedReader.lines().collect(Collectors.joining(NEWLINE));
        } catch (IOException ex) {
            throw new TemplateParseException(ex.getMessage(), ex);
        }
    }
}
