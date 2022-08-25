package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.magnit.magreportbackend.domain.reportjob.JobToken;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobTokenRepository extends JpaRepository<JobToken, String> {


    List<JobToken> findAllByCreatedBefore(LocalDateTime lastDateTime);

}
