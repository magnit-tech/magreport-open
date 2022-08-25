package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.util.Pair;
import ru.magnit.magreportbackend.util.Triple;

import javax.naming.directory.SearchControls;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LdapService {

    private final LdapTemplate ldapTemplate;

    @Value("#{'${ldap-settings.group-path}'.split(';')}")
    String[] ldapGroupPath;

    @Value("#{'${ldap-settings.user-path}'.split(';')}")
    String[] ldapUserPath;

    @Value("${ldap-settings.user-ldap-filter}")
    String ldapUserFilter;

    @Value("${ldap-settings.search-batch-size}")
    Long searchBatchSize;

    public List<String> getGroupsByNamePart(String namePart) {
        return Arrays.stream(ldapGroupPath)
                .map(path ->
                        ldapTemplate.search(
                                path,
                                "cn=*" + namePart + "*",
                                SearchControls.SUBTREE_SCOPE,
                                (AttributesMapper<String>) attributes -> String.valueOf(attributes.get("cn").get())))
                .flatMap(Collection::stream)
                .toList();
    }

    public String getUserFullName(String loginName) {

        var displayNames = Arrays.stream(ldapUserPath)
                .map(path ->
                        ldapTemplate.search(
                                path,
                                ldapUserFilter.replace("username", loginName),
                                SearchControls.SUBTREE_SCOPE,
                                (AttributesMapper<String>) attributes -> String.valueOf(attributes.get("displayName").get()))
                )
                .flatMap(Collection::stream)
                .filter(Predicate.not(String::isBlank))
                .toList();

        return displayNames.isEmpty() ? "" : displayNames.get(0);
    }

    public String getUserEmail(String loginName) {

        final var mails = Arrays.stream(ldapUserPath)
                .map(path ->
                        ldapTemplate.search(
                                path,
                                ldapUserFilter.replace("username", loginName),
                                SearchControls.SUBTREE_SCOPE,
                                (AttributesMapper<String>) attributes -> {
                                    try {
                                        return String.valueOf(attributes.get("mail").get());
                                    } catch (Exception ex) {
                                        return "";
                                    }
                                }))
                .flatMap(Collection::stream)
                .filter(Predicate.not(String::isBlank))
                .toList();

        return mails.isEmpty() ? "" : mails.get(0);
    }

    public Map<String, Pair<String, String>> getUserInfo(List<String> usernames) {

        var batch = new ArrayList<String>();
        var results = new HashMap<String, Pair<String, String>>();
        usernames.forEach(user -> {

            if (batch.size() < searchBatchSize)
                batch.add(user);
            else {
                results.putAll(searchInLdap(batch));
                batch.clear();
            }
        });

        results.putAll(searchInLdap(batch));
        return results;
    }

    private Map<String, Pair<String, String>> searchInLdap(List<String> logins) {

        var filter = "(|" + logins.stream()
                .map(login -> "(cn=" + login + ")")
                .collect(Collectors.joining()) + ")";

        return Arrays.stream(ldapUserPath)
                .map(path ->
                        ldapTemplate.search(
                                path,
                                filter,
                                SearchControls.SUBTREE_SCOPE,
                                (AttributesMapper<Triple<String, String, String>>) attributes -> {
                                    var result = new Triple<String, String, String>();
                                    result.setA(String.valueOf(attributes.get("cn").get()));
                                    try {
                                        result.setB(String.valueOf(attributes.get("mail").get()));
                                    } catch (Exception ex) {
                                        result.setB("");
                                    }
                                    result.setC(String.valueOf(attributes.get("displayName")));
                                    return result;
                                }))
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Triple::getA, t -> new Pair<String, String>().setL(t.getB()).setR(t.getC())));
    }
}
