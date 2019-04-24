package be.xplore.conference.service;

import be.xplore.conference.model.DayOfWeek;
import be.xplore.conference.model.Room;
import be.xplore.conference.model.RoomSchedule;
import be.xplore.conference.persistence.RoomScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoomScheduleService {
    private final RoomScheduleRepository repo;

    public RoomScheduleService(RoomScheduleRepository repo) {
        this.repo = repo;
    }

    public RoomSchedule save(RoomSchedule roomSchedule) {
        return repo.save(roomSchedule);
    }

    public Optional<RoomSchedule> loadByDateAndRoomId(LocalDate date, String roomId) {
        return repo.findByDateAndRoomId(date, roomId);
    }

    public Optional<RoomSchedule> loadByDayAndRoomId(DayOfWeek day, String roomId) {
        return repo.findByDayAndRoomId(day, roomId);
    }
}
