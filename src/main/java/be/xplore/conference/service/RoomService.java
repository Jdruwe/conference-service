package be.xplore.conference.service;

import be.xplore.conference.model.Room;
import be.xplore.conference.persistence.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private RoomRepository repo;

    public RoomService(RoomRepository repo) {
        this.repo = repo;
    }

    public Room save(Room room) {
        return repo.save(room);
    }

    public List<Room> loadAll(){
        return repo.findAll();
    }
}
