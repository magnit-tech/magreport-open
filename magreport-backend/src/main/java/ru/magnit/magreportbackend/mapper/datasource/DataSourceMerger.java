package ru.magnit.magreportbackend.mapper.datasource;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.datasource.DataSource;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolder;
import ru.magnit.magreportbackend.domain.datasource.DataSourceType;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceAddRequest;
import ru.magnit.magreportbackend.mapper.Merger;
import ru.magnit.magreportbackend.service.security.CryptoService;

@Service
@RequiredArgsConstructor
public class DataSourceMerger implements Merger<DataSource, DataSourceAddRequest> {

    private final CryptoService cryptoService;

    @Override
    public DataSource merge(DataSource target, DataSourceAddRequest source) {

        var merged = target == null ? new DataSource() : target;

        return merged
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
