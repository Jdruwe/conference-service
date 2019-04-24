package be.xplore.conference.persistence;

import be.xplore.conference.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, String> {
    List<Room> findAll();

    Optional<Room> findRoomById(String id);
}
