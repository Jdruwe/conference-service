package be.xplore.conference.service;

import be.xplore.conference.model.Schedule;
import be.xplore.conference.persistence.ScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
public class ScheduleService {
    private ScheduleRepository repo;

    public ScheduleService(ScheduleRepository repo) {
        this.repo = repo;
    }

    public Schedule save(Schedule schedule) {
        return repo.save(schedule);
    }

    public Optional<Schedule> loadById(LocalDate date) {
        return repo.findByIdWithRoomProxy(date);
    }

    public Optional<Schedule> loadByDateAndRoom(LocalDate date, String roomId) {
        return repo.findByDateAndRoom(date, roomId);
    }
}
