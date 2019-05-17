package be.xplore.conference.persistence;

import be.xplore.conference.Speaker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpeakerRepository extends JpaRepository<Speaker, String> {
}
