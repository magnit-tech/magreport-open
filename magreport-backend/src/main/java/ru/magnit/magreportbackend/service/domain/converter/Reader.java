package ru.magnit.magreportbackend.service.domain.converter;

import ru.magnit.magreportbackend.dto.inner.jobengine.CacheEntry;
import ru.magnit.magreportbackend.dto.inner.jobengine.CacheRow;
import ru.magnit.magreportbackend.dto.inner.olap.CubeData;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Reader extends AutoCloseable{

    CacheRow getRow();
    List<Map<Long, CacheEntry>> getAllRows(Set<Long> allFieldIds);
    CubeData getCube();
}
