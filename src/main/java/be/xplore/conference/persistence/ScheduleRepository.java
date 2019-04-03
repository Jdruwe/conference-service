package be.xplore.conference.persistence;

import be.xplore.conference.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT s FROM Schedule s JOIN s.rooms r ON ?2 = r.id WHERE s.date = ?1")
    Optional<Schedule> findByDateAndRoom(LocalDate date, String roomId);
}
