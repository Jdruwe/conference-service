package be.xplore.conference.persistence;

import be.xplore.conference.model.DayOfWeek;
import be.xplore.conference.model.RoomSchedule;
import be.xplore.conference.model.RoomScheduleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomScheduleRepository extends JpaRepository<RoomSchedule, RoomScheduleId> {
    @Query("SELECT rs FROM RoomSchedule  rs WHERE rs.id.room.id = ?2 AND rs.id.schedule.date = ?1")
    Optional<RoomSchedule> findByDateAndRoomId(LocalDate date, String roomId);

    @Query("SELECT rs FROM RoomSchedule rs LEFT JOIN FETCH rs.talks WHERE rs.id.room.id = ?2 AND rs.id.schedule.day = ?1")
    Optional<RoomSchedule> findByDayAndRoomId(DayOfWeek day, String roomId);
}
