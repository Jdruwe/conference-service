package be.xplore.conference.service;

import be.xplore.conference.excpetion.RoomAlreadyRegisteredException;
import be.xplore.conference.excpetion.RoomNotFoundException;
import be.xplore.conference.model.Client;
import be.xplore.conference.model.Room;
import be.xplore.conference.persistence.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
        Room room = roomService.loadById(client.getRoom().getId()).orElseThrow(() -> new RoomNotFoundException("No room found!"));
        client.setRoom(room);
        if (repo.findClientById(client.getId()).isEmpty()) {
            return repo.save(client);
        } else {
            throw new RoomAlreadyRegisteredException("This Room is already in use.");
        }
    }

    public int delete(int id) {
        return repo.deleteClientById(id);
    }


    public List<Client> loadAll() {
        return this.repo.findAll();
    }

    public Client updateLastConnectedTime(int id, LocalDateTime newDate) throws RoomNotFoundException {
        Client client = this.repo.findClientById(id).orElseThrow(() -> new RoomNotFoundException("No client found!"));
        client.setLastConnected(newDate);
        client = this.repo.save(client);
        return client;
    }
}
