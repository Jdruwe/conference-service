package be.xplore.conference.persistence;

import be.xplore.conference.model.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SettingsRepository extends JpaRepository<Settings, String> {


    @Query("SELECT s FROM Settings s where s.key = ?1")
    Settings findByKey(String key);

    @Query("SELECT s FROM Settings s")
    List<Settings> findAll();
}
