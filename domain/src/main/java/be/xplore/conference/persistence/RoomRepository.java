package be.xplore.conference.persistence;

import be.xplore.conference.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, String> {
    @Override
    List<Room> findAll();
}
