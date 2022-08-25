package ru.magnit.magreportbackend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import javax.naming.directory.SearchControls;

@Slf4j
@Configuration
public class LdapTemplateConfig {

    @Value("${spring.ldap.url}")
    private String url;

    @Value("${spring.ldap.base}")
    private String base;

    @Bean
    public ContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(url);
        contextSource.setBase(base);
        contextSource.setAnonymousReadOnly(true);
        contextSource.setPooled(true);
        contextSource.afterPropertiesSet();

        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        LdapTemplate ldapTemplate = new LdapTemplate(contextSource());
        ldapTemplate.setDefaultSearchScope(SearchControls.SUBTREE_SCOPE);
        ldapTemplate.setIgnorePartialResultException(true);
        return ldapTemplate;
    }
}