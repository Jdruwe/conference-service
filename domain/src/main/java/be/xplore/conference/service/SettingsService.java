package be.xplore.conference.service;

import be.xplore.conference.exception.SettingNotFoundException;
import be.xplore.conference.model.Settings;
import be.xplore.conference.persistence.SettingsRepository;
import be.xplore.conference.scheduler.ClientScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SettingsService {

    private static final String MAIL_DELAY_FOR_CONNECTION_ISSUES = "mailDelayForConnectionIssues";

    private final SettingsRepository repo;
    private final ClientScheduler clientScheduler;

    public SettingsService(SettingsRepository repo, ClientScheduler clientScheduler) {
        this.repo = repo;
        this.clientScheduler = clientScheduler;
    }

    public Settings save(Settings settings) {
        return repo.save(settings);
    }

    public List<Settings> loadAll() {
        return repo.findAll();
    }

    public Optional<Settings> loadByKey(String key) {
        return repo.findByKey(key);
    }

    public Settings update(String key, String newValue) {
        Settings settings = loadByKey(key)
                .orElseThrow(SettingNotFoundException::new);
        if (!settings.getValue().equals(newValue) && settings.getKey().equals(MAIL_DELAY_FOR_CONNECTION_ISSUES)) {
            resetClientScheduler(newValue);
        }
        settings.setValue(newValue);
        return save(settings);
    }

    private void resetClientScheduler(String newValue) {
        clientScheduler.stopScheduler();
        clientScheduler.startScheduler(newValue);
    }
}
