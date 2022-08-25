package ru.magnit.magreportbackend.mapper.datasource;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.datasource.DataSource;
import ru.magnit.magreportbackend.mapper.Cloner;

@Service
@RequiredArgsConstructor
public class DataSourceCloner implements Cloner<DataSource> {
    @Override
    public DataSource clone(DataSource source) {
        return new DataSource()
            .setName(source.getName())
            .setDescription(source.getDescription())
            .setPassword(source.getPassword())
            .setPoolSize(source.getPoolSize())
            .setType(source.getType())
            .setUrl(source.getUrl())
            .setUserName(source.getUserName());
    }
}
