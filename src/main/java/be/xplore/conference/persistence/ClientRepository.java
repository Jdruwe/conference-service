package be.xplore.conference.persistence;

import be.xplore.conference.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface ClientRepository extends JpaRepository<Client, Integer> {

    @Query("SELECT c FROM Client c WHERE c.id = ?1")
    Optional<Client> findClientById(int id);

    @Modifying
    @Query("DELETE FROM Client c WHERE c.id = ?1")
    int deleteClientById(int id);

    List<Client> findAll();
    
    
}
