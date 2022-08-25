package ru.magnit.magreportbackend.domain.schedule;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplate;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ScheduleTaskTest extends BaseEntityTest {

    private final static Long ID = 0L;
    private final static String CODE = "code";
    private final static String MESSAGE_MAIL = "message mail";
    private final static UUID EXPIRATION_CODE = UUID.randomUUID();
    private final static LocalDate EXPIRATION_DATE = LocalDate.now();
    private final static Long RENEWAL_PERIOD = 180L;
    private final static String NAME = "name";
    private final static String DESCRIPTION = "description";
    private final static LocalDateTime CREATED = LocalDateTime.now();
    private final static LocalDateTime MODIFIED = LocalDateTime.now().plusDays(2);

    @BeforeAll
    void init() throws ClassNotFoundException {
        checkNumberOfFields(26);
    }

    @Test
    void testNotArgs() {

        var response = getScheduleTask();

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CODE, response.getCode());
        assertEquals(MESSAGE_MAIL, response.getReportBodyMail());
        assertEquals(CREATED, response.getCreatedDateTime());
        assertEquals(MODIFIED, response.getModifiedDateTime());
        assertEquals(RENEWAL_PERIOD, response.getRenewalPeriod());
        assertEquals(MESSAGE_MAIL, response.getReportBodyMail());
        assertEquals(EXPIRATION_CODE, response.getExpirationCode());
        assertEquals(EXPIRATION_DATE, response.getExpirationDate());

        assertNotNull(response.getReport());
        assertNotNull(response.getUser());
        assertNotNull(response.getReportJobFilters());
        assertNotNull(response.getExcelTemplate());
        assertNotNull(response.getStatus());
        assertNotNull(response.getTaskType());
        assertNotNull(response.getEmailDestinations());
        assertNotNull(response.getUserDestinations());
        assertNotNull(response.getRoleDestinations());
        assertNotNull(response.toString());
    }

    @Test
    void testIdArgs() {
        var response = new ScheduleTask(ID);
        assertEquals(ID, response.getId());
    }

    @Test
    void testEquals() {
        var response1 = new ScheduleTask(ID);
        var response2 = new ScheduleTask(ID);
        assertEquals(response1, response2);
    }

    private ScheduleTask getScheduleTask() {
        return new ScheduleTask()
                .setId(ID)
                .setCode(CODE)
                .setExpirationCode(EXPIRATION_CODE)
                .setExpirationDate(EXPIRATION_DATE)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreatedDateTime(CREATED)
                .setModifiedDateTime(MODIFIED)
                .setRenewalPeriod(RENEWAL_PERIOD)
                .setReportBodyMail(MESSAGE_MAIL)
                .setReport(new Report())
                .setStatus(new ScheduleTaskStatus())
                .setTaskType(new ScheduleTaskType())
                .setUser(new User())
                .setExcelTemplate(new ExcelTemplate())
                .setEmailDestinations(Collections.emptyList())
                .setUserDestinations(Collections.emptyList())
                .setRoleDestinations(Collections.emptyList())
                .setReportJobFilters(Collections.emptyList())
                .setScheduleList(Collections.emptyList());

    }

}
