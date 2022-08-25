package ru.magnit.magreportbackend.service.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class LdapServiceTest {

    @Mock
    private LdapTemplate ldapTemplate;

    @InjectMocks
    private LdapService service;

    private final static String[] USER_LDAP_PATH = {""};
    private final static String[] GROUP_LDAP_PATH = {""};
    private final static String USER_FILTER_LDAP = "";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "ldapUserPath", USER_LDAP_PATH);
        ReflectionTestUtils.setField(service, "ldapGroupPath", GROUP_LDAP_PATH);
        ReflectionTestUtils.setField(service, "ldapUserFilter", USER_FILTER_LDAP);
    }

    @Test
    @SuppressWarnings("unchecked")
    void getGroupsByNamePart() {
        assertNotNull(service.getGroupsByNamePart(""));
        verify(ldapTemplate).search(nullable(String.class), any(), anyInt(), (AttributesMapper<Object>) any());
        verifyNoMoreInteractions(ldapTemplate);
    }

    @Test
    @SuppressWarnings("unchecked")
    void getUserFullName() {
        assertNotNull(service.getUserFullName(""));
        verify(ldapTemplate).search(nullable(String.class), any(), anyInt(), (AttributesMapper<Object>) any());
        verifyNoMoreInteractions(ldapTemplate);
    }

    @Test
    @SuppressWarnings("unchecked")
    void getUserEmail() {
        assertNotNull(service.getUserEmail(""));
        verify(ldapTemplate).search(nullable(String.class), any(), anyInt(), (AttributesMapper<Object>) any());
        verifyNoMoreInteractions(ldapTemplate);
    }
}
