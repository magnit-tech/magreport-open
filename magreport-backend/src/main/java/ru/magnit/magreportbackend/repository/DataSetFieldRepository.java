package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;

import java.util.List;

public interface DataSetFieldRepository extends JpaRepository<DataSetField, Long> {

    @Modifying
    @Query("update DATASET_FIELD set isSync=:sync where id=:fieldId")
    void markFieldSync(@Param("fieldId") Long fieldId, @Param("sync") Boolean isValid);

    @Modifying
    @Query("update DATASET_FIELD set type.id=:typeId where id=:fieldId")
    void updateDataTypeField(@Param("fieldId") Long fieldId, @Param("typeId") Long typeId);

    void deleteAllByIdIn(List<Long> idList);
}
