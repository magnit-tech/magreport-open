package ru.magnit.magreportbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.magnit.magreportbackend.domain.filterreport.GroupOperationTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;
import ru.magnit.magreportbackend.dto.request.filterinstance.ChildNodesRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.ListValuesRequest;
import ru.magnit.magreportbackend.dto.request.filterreport.FilterGroupAddRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportRequest;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterNodeResponse;
import ru.magnit.magreportbackend.dto.response.filterreport.*;
import ru.magnit.magreportbackend.dto.tuple.Tuple;
import ru.magnit.magreportbackend.service.FilterReportService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

@ExtendWith(MockitoExtension.class)
class FilterReportControllerTest {

    private static final long GROUP_ID = 1L;
    private static final long REPORT_ID = 1L;
    private static final String NAME = "Test filter group";
    private static final String CODE = "Test code";
    private static final String DESCRIPTION = "Test filter group description";
    private static final String USER_NAME = "TestUser";
    private static final long ORDINAL = 1L;
    private static final Long ID = 3L;
    private static final Long FILTER_INSTANCE_ID = 2L;
    private static final Integer ORDINAL_INT = 5;
    private static final FilterTypeEnum TYPE = FilterTypeEnum.RANGE;
    private static final Boolean HIDDEN = false;
    private static final Boolean MANDATORY = true;
    private static final Boolean ROOT_SELECTABLE = false;
    private static final LocalDateTime CREATED_DATE = LocalDateTime.now().minusDays(3L);
    private static final LocalDateTime MODIFIED_DATE = LocalDateTime.now();
    private static final Boolean EXPAND = false;

    @Mock
    private FilterReportService service;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private FilterReportController controller;

    private MockMvc mockMvc;

    private ObjectMapper mapper;

    private DateTimeFormatter formatter;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mapper = new ObjectMapper();
        formatter = DateTimeFormatter.ofPattern(ISO_DATE_TIME_PATTERN);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USER_NAME);
    }

    @Test
    void addFilters() throws Exception {

        given(service.addFilters(any())).willReturn(getResponse());
        mockMvc
                .perform(post(FilterReportController.REPORT_SET_FILTERS)
                        .content(mapper.writeValueAsString(getAddRequest()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.id", is((int) GROUP_ID)))
                .andExpect(jsonPath("$.data.name", is(NAME)))
                .andExpect(jsonPath("$.data.description", is(DESCRIPTION)));

        verify(service).addFilters(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void getFilters() throws Exception {

        given(service.getFilters(any())).willReturn(getResponse());
        mockMvc
                .perform(post(FilterReportController.REPORT_GET_FILTERS)
                        .content(mapper.writeValueAsString(getGetRequest()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.id", is((int) GROUP_ID)))
                .andExpect(jsonPath("$.data.name", is(NAME)))
                .andExpect(jsonPath("$.data.description", is(DESCRIPTION)));

        verify(service).getFilters(any());
        verifyNoMoreInteractions(service);
    }

    private ReportRequest getGetRequest() {
        return new ReportRequest()
                .setId(GROUP_ID);
    }

    private FilterGroupResponse getResponse() {

        return new FilterGroupResponse(
                GROUP_ID,
                REPORT_ID,
                CODE,
                NAME,
                DESCRIPTION,
                ORDINAL,
                false,
                false,
                GroupOperationTypeEnum.AND,
                Collections.emptyList(),
                Collections.emptyList(),
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    private FilterGroupAddRequest getAddRequest() {

        return new FilterGroupAddRequest()
                .setId(GROUP_ID)
                .setReportId(REPORT_ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setLinkedFilters(false)
                .setOperationType(GroupOperationTypeEnum.AND)
                .setFilters(Collections.emptyList())
                .setChildGroups(Collections.emptyList());
    }

    @Test
    void getFilterReportValues() throws Exception {
        when(service.getFilterReportValues(any())).thenReturn(getFilterReportValuesResponse());

        mockMvc
                .perform(post(FilterReportController.REPORT_FILTER_GET_VALUES)
                        .content(mapper.writeValueAsString(getListValuesRequest()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.tuples", hasSize(1)))
                .andExpect(jsonPath("$.data.filter.fields", hasSize(1)))
                .andExpect(jsonPath("$.data.filter.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.filter.name", is(NAME)))
                .andExpect(jsonPath("$.data.filter.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data.filter.userName", is(USER_NAME)))
                .andExpect(jsonPath("$.data.filter.filterInstanceId", is(FILTER_INSTANCE_ID.intValue())))
                .andExpect(jsonPath("$.data.filter.hidden", is(HIDDEN)))
                .andExpect(jsonPath("$.data.filter.mandatory", is(MANDATORY)))
                .andExpect(jsonPath("$.data.filter.ordinal", is(ORDINAL_INT)))
                .andExpect(jsonPath("$.data.filter.rootSelectable", is(ROOT_SELECTABLE)))
                .andExpect(jsonPath("$.data.filter.type", is(TYPE.name())))
                .andExpect(jsonPath("$.data.filter.created", is(formatter.format(CREATED_DATE))))
                .andExpect(jsonPath("$.data.filter.modified", is(formatter.format(MODIFIED_DATE))));

        verify(service).getFilterReportValues(any());
        verifyNoMoreInteractions(service);
    }

    private ListValuesRequest getListValuesRequest() {
        return new ListValuesRequest();
    }

    private FilterReportValuesResponse getFilterReportValuesResponse() {
        return new FilterReportValuesResponse(
                new FilterReportResponse(
                        ID,
                        FILTER_INSTANCE_ID,
                        TYPE,
                        HIDDEN,
                        MANDATORY,
                        ROOT_SELECTABLE,
                        NAME,
                        CODE,
                        DESCRIPTION,
                        ORDINAL_INT,
                        Collections.singletonList(new FilterReportFieldResponse(
                                ID, NAME, DESCRIPTION, FilterFieldTypeEnum.CODE_FIELD, 1L, ORDINAL, ID, ID, CREATED_DATE, MODIFIED_DATE, EXPAND
                        )),
                        USER_NAME,
                        CREATED_DATE,
                        MODIFIED_DATE
                ),
                Collections.singletonList(new Tuple())
        );
    }

    @Test
    void getFilterReportChildNodes() throws Exception {
        when(service.getChildNodes(any())).thenReturn(getFilterReportChildNodesResponse());

        mockMvc
                .perform(post(FilterReportController.REPORT_FILTER_GET_CHILD_NODES)
                        .content(mapper.writeValueAsString(getChildNodesRequest()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.rootNode.fieldId", is(ID.intValue())))
                .andExpect(jsonPath("$.data.fieldId", is(ID.intValue())))
                .andExpect(jsonPath("$.data.filter.id", is(ID.intValue())));

        verify(service).getChildNodes(any());
        verifyNoMoreInteractions(service);
    }

    private ChildNodesRequest getChildNodesRequest() {
        return new ChildNodesRequest()
                .setFilterId(ID);
    }

    private FilterReportChildNodesResponse getFilterReportChildNodesResponse() {
        return new FilterReportChildNodesResponse(
                new FilterReportResponse(
                        ID,
                        FILTER_INSTANCE_ID,
                        TYPE,
                        HIDDEN,
                        MANDATORY,
                        ROOT_SELECTABLE,
                        NAME,
                        CODE,
                        DESCRIPTION,
                        ORDINAL_INT,
                        Collections.singletonList(new FilterReportFieldResponse(
                                ID, NAME, DESCRIPTION, FilterFieldTypeEnum.CODE_FIELD, 1L, ORDINAL, ID, ID, CREATED_DATE, MODIFIED_DATE,EXPAND
                        )),
                        USER_NAME,
                        CREATED_DATE,
                        MODIFIED_DATE
                ),
                ID,
                new FilterNodeResponse(ID, 1L, "ID", NAME, Collections.emptyList())
        );
    }
}