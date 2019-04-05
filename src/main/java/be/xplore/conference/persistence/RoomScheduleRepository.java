package be.xplore.conference.persistence;

import be.xplore.conference.model.RoomSchedule;
import be.xplore.conference.model.RoomScheduleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomScheduleRepository extends JpaRepository<RoomSchedule, RoomScheduleId> {
}
