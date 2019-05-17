package be.xplore.conference.persistence;

import be.xplore.conference.model.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, LocalDate> {
    @Query("SELECT s FROM Schedule s WHERE s.date = ?1")
    Optional<Schedule> findByIdWithRoomProxy(LocalDate date);
}
