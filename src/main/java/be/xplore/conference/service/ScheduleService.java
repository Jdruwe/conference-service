package be.xplore.conference.service;

import be.xplore.conference.model.Schedule;
import be.xplore.conference.persistence.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ScheduleService {
    private ScheduleRepository repo;

    public ScheduleService(ScheduleRepository repo) {
        this.repo = repo;
    }

    public Schedule save(Schedule schedule) {
        return repo.save(schedule);
    }

    public Optional<Schedule> loadByDateAndRoom(LocalDate date, String roomId) {
        return repo.findByDateAndRoom(date, roomId);
    }
}
