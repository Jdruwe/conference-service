package be.xplore.conference.service;

import be.xplore.conference.model.Schedule;
import be.xplore.conference.persistence.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
public class ScheduleService {
    private final ScheduleRepository repo;

    public ScheduleService(ScheduleRepository repo) {
        this.repo = repo;
    }

    public Schedule save(Schedule schedule) {
        return repo.save(schedule);
    }

    public Optional<Schedule> loadById(LocalDate date) {
        return repo.findByIdWithRoomProxy(date);
    }
}
