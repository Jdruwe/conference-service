package be.xplore.conference.persistence;

import be.xplore.conference.model.Talk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TalkRepository extends JpaRepository<Talk, String> {
    @Override
    @Query("SELECT t FROM Talk t LEFT JOIN FETCH t.speakers WHERE t.id = ?1")
    Optional<Talk> findById(String id);
}
