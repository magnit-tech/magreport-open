package ru.magnit.magreportbackend.mapper.datasource;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.datasource.DataSource;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolder;
import ru.magnit.magreportbackend.domain.datasource.DataSourceType;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.service.security.CryptoService;

@Service
@RequiredArgsConstructor
public class DataSourceMapper implements Mapper<DataSource, DataSourceAddRequest> {

    private final CryptoService cryptoService;

    @Override
    public DataSource from(DataSourceAddRequest source) {
        return mapBaseProperties(source);
    }

    private DataSource mapBaseProperties(DataSourceAddRequest source) {
        return new DataSource()
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setFolder(new DataSourceFolder(source.getFolderId()))
                .setType(new DataSourceType(source.getTypeId()))
                .setUrl(source.getUrl())
                .setUserName(source.getUserName())
                .setPassword(cryptoService.encode(source.getPassword()))
                .setPoolSize(source.getPoolSize() == null ? 5 : source.getPoolSize());
    }
}
