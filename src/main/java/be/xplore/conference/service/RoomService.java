package be.xplore.conference.service;

import be.xplore.conference.model.Room;
import be.xplore.conference.persistence.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
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

    public Optional<Room> loadById(String id){
        return repo.findById(id);
    }
}
