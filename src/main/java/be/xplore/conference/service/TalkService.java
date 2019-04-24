package be.xplore.conference.service;

import be.xplore.conference.model.Talk;
import be.xplore.conference.persistence.TalkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TalkService {
    private TalkRepository repo;

    public TalkService(TalkRepository repo) {
        this.repo = repo;
    }

    public Talk save(Talk talk) {
        return repo.save(talk);
    }

    public Optional<Talk> loadById(String id) {
        return repo.findById(id);
    }
}
