package ru.magnit.magreportbackend.mapper.securityfilter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilter;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterRole;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.dto.request.securityfilter.RoleSettingsRequest;
import ru.magnit.magreportbackend.dto.request.securityfilter.SecurityFilterSetRoleRequest;
import ru.magnit.magreportbackend.exception.NotImplementedException;
import ru.magnit.magreportbackend.mapper.Mapper;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SecurityFilterRoleMapper implements Mapper<SecurityFilterRole, SecurityFilterSetRoleRequest> {

    private final SecurityFilterRoleTupleMapper roleTupleMapper;

    @Override
    public SecurityFilterRole from(SecurityFilterSetRoleRequest source) {

        throw new NotImplementedException("Wrong mapper function called.");
    }

    @Override
    public List<SecurityFilterRole> from(List<SecurityFilterSetRoleRequest> sources) {

        final var request = sources.get(0);

        return request.getRoleSettings()
            .stream()
            .map(roleSet -> getSecurityFilterRole(request, roleSet))
            .collect(Collectors.toCollection(LinkedList::new));
    }

    private SecurityFilterRole getSecurityFilterRole(SecurityFilterSetRoleRequest request, RoleSettingsRequest roleSet) {
        final var filterRole = new SecurityFilterRole()
            .setSecurityFilter(new SecurityFilter(request.getSecurityFilterId()))
            .setRole(new Role(roleSet.getRoleId()))
            .setFilterRoleTuples(roleTupleMapper.from(roleSet.getTuples()));
        filterRole.getFilterRoleTuples().forEach(tuple -> tuple.setSecurityFilterRole(filterRole));

        return filterRole;
    }
}
