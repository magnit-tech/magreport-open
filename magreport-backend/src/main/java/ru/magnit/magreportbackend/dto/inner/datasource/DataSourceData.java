package ru.magnit.magreportbackend.dto.inner.datasource;

import ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum;

import java.util.Objects;

public record DataSourceData(
        Long id,
        DataSourceTypeEnum type,
        String url,
        String userName,
        String password,
        Short poolSize
) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataSourceData that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
