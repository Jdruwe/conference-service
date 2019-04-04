package be.xplore.conference.persistence;

import be.xplore.conference.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, LocalDate> {
    @Query("SELECT s FROM Schedule s LEFT JOIN FETCH s.rooms r WHERE s.date = ?1 AND r.id = ?2")
    Optional<Schedule> findByDateAndRoom(LocalDate date, String roomId);

    @Query("SELECT s FROM Schedule s LEFT JOIN FETCH s.rooms WHERE s.date = ?1")
    Optional<Schedule> findByIdWithRoomProxy(LocalDate date);
}
