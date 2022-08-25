package ru.magnit.magreportbackend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;
import ru.magnit.magreportbackend.domain.serversettings.ServerMailTemplateTypeEnum;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.request.serversettings.ServerMailTemplateEditRequest;
import ru.magnit.magreportbackend.dto.request.serversettings.ServerMailTemplateRequest;
import ru.magnit.magreportbackend.dto.request.serversettings.ServerMailTemplateTypeRequest;
import ru.magnit.magreportbackend.dto.response.serversettings.ServerMailTemplateResponse;
import ru.magnit.magreportbackend.dto.response.serversettings.ServerMailTemplateTypeResponse;
import ru.magnit.magreportbackend.service.domain.ServerMailTemplateDomainService;
import ru.magnit.magreportbackend.service.domain.ServerMailTemplateTypeDomainService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServerMailTemplateServiceTest {

    private final static Long ID = 0L;
    private final static String NAME = "NAME";
    private final static String DESCRIPTION = "DESCRIPTION";
    private final static String SUBJECT = "SUBJECT";
    private final static String BODY = "BODY";
    private final static String CODE = "CODE";
    private final static LocalDateTime CREATED = LocalDateTime.now();
    private final static LocalDateTime MODIFIED = LocalDateTime.now();

    @Mock
    private ServerMailTemplateDomainService serverMailTemplateDomainService;
    @Mock
    private ServerMailTemplateTypeDomainService serverMailTemplateTypeDomainService;

    @Mock
    private UserDomainService userDomainService;

    @InjectMocks
    private ServerMailTemplateService service;


    @Test
    void getServerMailTemplateType() {

        when(serverMailTemplateTypeDomainService.getServerMailTemplateType(anyLong())).thenReturn(getServerMailTemplateTypeResponse());

        var response = service.getServerMailTemplateType(getServerMailTemplateTypeRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CREATED, response.getCreated());
        assertEquals(MODIFIED, response.getModified());
        assertEquals(0, response.getChildTypes().size());
        assertEquals(1, response.getSystemMailTemplates().size());


        verify(serverMailTemplateTypeDomainService).getServerMailTemplateType(anyLong());
        verifyNoMoreInteractions(serverMailTemplateTypeDomainService);
        verifyNoInteractions(serverMailTemplateDomainService);

    }

    @Test
    void getAllServerMailTemplateType() {

        when(serverMailTemplateTypeDomainService.getAllServerMailTemplateType())
                .thenReturn(Collections.singletonList(getServerMailTemplateTypeResponse()));

        var responses = service.getAllServerMailTemplateType();

        assertFalse(responses.isEmpty());
        var response = responses.get(0);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CREATED, response.getCreated());
        assertEquals(MODIFIED, response.getModified());
        assertEquals(0, response.getChildTypes().size());
        assertEquals(1, response.getSystemMailTemplates().size());


        verify(serverMailTemplateTypeDomainService).getAllServerMailTemplateType();
        verifyNoMoreInteractions(serverMailTemplateTypeDomainService);
        verifyNoInteractions(serverMailTemplateDomainService);

    }

    @Test
    void editServerMailTemplate() {

        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setId(0L));

        service.editServerMailTemplate(getServerMailTemplateEditRequest());

        verify(serverMailTemplateDomainService).editServerMailTemplate(any(),any());
        verifyNoMoreInteractions(serverMailTemplateDomainService);
        verifyNoInteractions(serverMailTemplateTypeDomainService);
    }

    @Test
    void getServerMailTemplate() {

        when(serverMailTemplateDomainService.getServerMailTemplate(anyLong())).thenReturn(getServerMailTemplateResponse());

        var response = service.getServerMailTemplate(getServerMailTemplateRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CREATED, response.getCreated());
        assertEquals(MODIFIED, response.getModified());
        assertEquals(BODY, response.getBody());
        assertEquals(SUBJECT, response.getSubject());
        assertEquals(ID, response.getType().getId());
        assertEquals(CODE, response.getCode());


        verify(serverMailTemplateDomainService).getServerMailTemplate(anyLong());
        verifyNoMoreInteractions(serverMailTemplateDomainService);
        verifyNoInteractions(serverMailTemplateTypeDomainService);
    }

    @Test
    void getServerMailTemplateByType() {

        when(serverMailTemplateDomainService.getServerMailTemplateByType(anyLong()))
                .thenReturn(Collections.singletonList(getServerMailTemplateResponse()));

        var responses = service.getServerMailTemplateByType(getServerMailTemplateTypeRequest());
        assertFalse(responses.isEmpty());

        var response = responses.get(0);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CREATED, response.getCreated());
        assertEquals(MODIFIED, response.getModified());
        assertEquals(BODY, response.getBody());
        assertEquals(SUBJECT, response.getSubject());
        assertEquals(ID, response.getType().getId());
        assertEquals(CODE, response.getCode());


        verify(serverMailTemplateDomainService).getServerMailTemplateByType(anyLong());
        verifyNoMoreInteractions(serverMailTemplateDomainService);
        verifyNoInteractions(serverMailTemplateTypeDomainService);
    }

    @Test
    void getAllServerMailTemplate() {

        when(serverMailTemplateDomainService.getAllServerMailTemplate())
                .thenReturn(Collections.singletonList(getServerMailTemplateResponse()));

        var responses = service.getAllServerMailTemplate();
        assertFalse(responses.isEmpty());

        var response = responses.get(0);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CREATED, response.getCreated());
        assertEquals(MODIFIED, response.getModified());
        assertEquals(BODY, response.getBody());
        assertEquals(SUBJECT, response.getSubject());
        assertEquals(ID, response.getType().getId());
        assertEquals(CODE, response.getCode());


        verify(serverMailTemplateDomainService).getAllServerMailTemplate();
        verifyNoMoreInteractions(serverMailTemplateDomainService);
        verifyNoInteractions(serverMailTemplateTypeDomainService);
    }

    private ServerMailTemplateTypeRequest getServerMailTemplateTypeRequest() {
        return new ServerMailTemplateTypeRequest()
                .setId(ID);
    }

    private ServerMailTemplateEditRequest getServerMailTemplateEditRequest() {
        return new ServerMailTemplateEditRequest()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setSubject(SUBJECT)
                .setBody(BODY);
    }

    private ServerMailTemplateRequest getServerMailTemplateRequest() {
        return new ServerMailTemplateRequest()
                .setId(ID);
    }

    private ServerMailTemplateTypeResponse getServerMailTemplateTypeResponse() {
        return new ServerMailTemplateTypeResponse()
                .setId(ID)
                .setParentId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildTypes(Collections.emptyList())
                .setSystemMailTemplates(Collections.singletonList(getServerMailTemplateResponse()))
                .setCreated(CREATED)
                .setModified(MODIFIED);
    }

    private ServerMailTemplateResponse getServerMailTemplateResponse() {
        return new ServerMailTemplateResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setSubject(SUBJECT)
                .setBody(BODY)
                .setType(ServerMailTemplateTypeEnum.getById(ID))
                .setCode(CODE)
                .setCreated(CREATED)
                .setModified(MODIFIED);
    }
}
