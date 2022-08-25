package ru.magnit.magreportbackend.domain.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.excel.UserReportExcelTemplate;
import ru.magnit.magreportbackend.domain.folderreport.FolderReport;
import ru.magnit.magreportbackend.domain.reportjob.ReportJob;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Тестируются:
 * 1. Все поля типа Collection инициализированы;
 * 2. Имеется конструктор, принимающий Long;
 * 3. Конструктор из п.2 инициализирует id;
 * 4. Геттеры и сеттеры инциализируют и возвращают соответствующие поля;
 * 5. Количество полей класса под тестами не изменилось с момента написания тестов;
 * <p>
 * Геттеры и сеттеры полей типа Entity тестируются только на not null после инициализации;
 */
class UserTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "User 1";
    private static final String DESCRIPTION = "User one";
    private static final String EMAIL = "user1@mail.ru";
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final String FIRST_NAME = "Ivan";
    private static final String PATRONYMIC = "Ivanovich";
    private static final String LAST_NAME = "Ivanov";

    /**
     * Количество полей включает в себя serialVersionUID
     * но не включает поля родительского класса
     *
     * @throws ClassNotFoundException - может быть выброшено рефлексией
     */
    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(18);
    }

    @Test
    void testNoArgsConstructor() {
        var user = getUser();
        var user2 = new User(-ID);

        assertEquals(ID, user.getId());
        assertEquals(NAME, user.getName());
        assertEquals(DESCRIPTION, user.getDescription());
        assertEquals(FIRST_NAME, user.getFirstName());
        assertEquals(PATRONYMIC, user.getPatronymic());
        assertEquals(LAST_NAME, user.getLastName());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(NOW, user.getCreatedDateTime());

        assertEquals(NOW.plusMinutes(2), user.getModifiedDateTime());
        assertNotNull(user.getUserStatus());
        assertNotNull(user.getUserRoles());
        assertNotNull(user.getUserReportExcelTemplates());
        assertNotNull(user.getReportJobs());
        assertNotNull(user.getFolderReports());

        assertEquals(-ID, user2.getId());
    }

    private static User getUser() {
        return new User()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setFirstName(FIRST_NAME)
                .setPatronymic(PATRONYMIC)
                .setLastName(LAST_NAME)
                .setEmail(EMAIL)
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW.plusMinutes(2))
                .setUserStatus(new UserStatus())
                .setReportJobs(Collections.singletonList(new ReportJob()))
                .setUserRoles(Collections.singletonList(new UserRole()))
                .setUserReportExcelTemplates(Collections.singletonList(new UserReportExcelTemplate()))
                .setFolderReports(Collections.singletonList(new FolderReport()));
    }
}