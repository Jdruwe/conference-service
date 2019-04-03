package be.xplore.conference.persistence;

import be.xplore.conference.model.Talk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TalkRepository extends JpaRepository<Talk, String> {
}
