package be.xplore.conference.service;

import be.xplore.conference.excpetion.RoomAlreadyRegisteredException;
import be.xplore.conference.excpetion.RoomNotFoundException;
import be.xplore.conference.model.Client;
import be.xplore.conference.model.Room;
import be.xplore.conference.persistence.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClientService {

    private ClientRepository repo;
    private RoomService roomService;

    public ClientService(ClientRepository repo, RoomService roomService) {
        this.repo = repo;
        this.roomService = roomService;
    }

    public Client save(Client client) throws RoomAlreadyRegisteredException, RoomNotFoundException {
        Room room = roomService.loadRoomById(client.getRoom().getId());
        client.setRoom(room);
        if (repo.findClientByRoom(client.getRoom()).isEmpty()) {
            return repo.save(client);
        } else {
            throw new RoomAlreadyRegisteredException("This Room is already in use.");
        }
    }

    public void delete(String id) throws RoomNotFoundException {
        Room room = roomService.loadRoomById(id);
        repo.deleteClientByRoom(room);
    }
}
