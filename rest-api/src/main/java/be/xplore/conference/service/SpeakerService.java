package be.xplore.conference.service;

import be.xplore.conference.Speaker;
import be.xplore.conference.persistence.SpeakerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class SpeakerService {

    private final SpeakerRepository repo;

    public SpeakerService(SpeakerRepository repo) {
        this.repo = repo;
    }

    public Speaker save(Speaker speaker) {
        return repo.save(speaker);
    }

    public Optional<Speaker> loadById(String uuid) {
        return repo.findById(uuid);
    }
}
