package be.xplore.conference.service;

import be.xplore.conference.model.RoomSchedule;
import be.xplore.conference.persistence.RoomScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
}
