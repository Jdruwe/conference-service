package be.xplore.conference.persistence;

import be.xplore.conference.model.Client;
import be.xplore.conference.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface ClientRepository extends JpaRepository<Client, Integer> {

    @Query("SELECT c FROM Client c WHERE c.room = ?1")
    Optional<Client> findClientByRoom(Room room );

    @Modifying
    @Query("DELETE FROM Client c WHERE c.room = ?1")
    int deleteClientByRoom(Room room);

    List<Client> findAll();
    
    
}
