package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.magnit.magreportbackend.domain.theme.Theme;

import java.util.List;

public interface ThemeRepository extends JpaRepository<Theme,Long> {

    @Modifying
    @Query("update THEME t set t.favorite = false where t.type.id = :typeId and t.favorite = true")
    void clearFavoriteTheme(@Param("typeId") Long typeId);

    List<Theme> findAllByUserId (Long userId);

}
