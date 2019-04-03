package be.xplore.conference.service;

import be.xplore.conference.model.Schedule;
import be.xplore.conference.persistence.ScheduleRepository;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {
    private ScheduleRepository repo;

    public ScheduleService(ScheduleRepository repo) {
        this.repo = repo;
    }

    public Schedule save(Schedule schedule) {
        return repo.save(schedule);
    }
}
