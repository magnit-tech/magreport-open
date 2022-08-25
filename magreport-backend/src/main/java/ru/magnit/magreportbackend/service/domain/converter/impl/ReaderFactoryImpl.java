package ru.magnit.magreportbackend.service.domain.converter.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobData;
import ru.magnit.magreportbackend.service.domain.converter.Reader;
import ru.magnit.magreportbackend.service.domain.converter.ReaderFactory;

import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class ReaderFactoryImpl implements ReaderFactory {

    @Override
    public Reader createReader(ReportJobData data, Path inputPath) {
        return new AvroReader(data, inputPath);
    }
}
