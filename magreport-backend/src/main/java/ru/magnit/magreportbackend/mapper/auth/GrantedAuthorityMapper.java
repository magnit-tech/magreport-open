package ru.magnit.magreportbackend.mapper.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.mapper.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GrantedAuthorityMapper implements Mapper<GrantedAuthority, RoleView> {

    @Override
    public GrantedAuthority from(RoleView source) {
        return source::getName;
    }

    @Override
    public List<GrantedAuthority> from(List<RoleView> sources) {
        return sources.stream().map(this::from).collect(Collectors.toList());
    }
}
