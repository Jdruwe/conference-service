package be.xplore.conference.service;

import be.xplore.conference.model.Settings;
import be.xplore.conference.persistence.SettingsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SettingsService {

    private SettingsRepository repo;

    public SettingsService(SettingsRepository repo) {
        this.repo = repo;
    }

    public Settings save(Settings settings) {
        return repo.save(settings);
    }

    public List<Settings> loadAll() {
        return repo.findAll();
    }

    public Settings loadByKey(String key) {
        return repo.findByKey(key);
    }
}
