package ru.magnit.magreportbackend.mapper.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.mapper.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleViewMapper implements Mapper<RoleView, Role> {

    @Override
    public RoleView from(Role source) {
        return mapBaseProperties(source);
    }

    @Override
    public List<RoleView> from(List<Role> sources) {
        return sources.stream().map(this::from).collect(Collectors.toList());
    }

    private RoleView mapBaseProperties(Role source) {
        return new RoleView()
                .setId(source.getId())
                .setName(source.getName())
                .setDescription(source.getDescription());
    }
}
