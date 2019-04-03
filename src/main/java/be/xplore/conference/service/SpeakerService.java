package be.xplore.conference.service;

import be.xplore.conference.model.Speaker;
import be.xplore.conference.persistence.SpeakerRepository;
import org.springframework.stereotype.Service;

@Service
public class SpeakerService {

    private SpeakerRepository repo;

    public SpeakerService(SpeakerRepository repo) {
        this.repo = repo;
    }

    public Speaker save(Speaker speaker) {
        return repo.save(speaker);
    }
}