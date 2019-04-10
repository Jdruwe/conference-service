package be.xplore.conference.service;

import be.xplore.conference.excpetion.RoomAlreadyRegisteredException;
import be.xplore.conference.excpetion.RoomNotFoundException;
import be.xplore.conference.model.Room;
import be.xplore.conference.persistence.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public Room loadRoomById(String id) throws RoomNotFoundException {
        return repo.findRoomById(id).orElseThrow(() -> new RoomNotFoundException("This Room does not exist."));
    }
}
