package be.xplore.conference.rest.controller;

import be.xplore.conference.exception.ClientNotFoundException;
import be.xplore.conference.exception.RoomNotFoundException;
import be.xplore.conference.listener.ClientEventListener;
import be.xplore.conference.model.Client;
import be.xplore.conference.rest.dto.ClientDto;
import be.xplore.conference.rest.dto.ClientHeartbeatDto;
import be.xplore.conference.rest.dto.ClientInfoDto;
import be.xplore.conference.scheduler.ClientScheduler;
import be.xplore.conference.service.ClientService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/client")
public class ClientController {

    private final ClientService clientService;
    private final ModelMapper modelMapper;
    private final List<ClientEventListener> listeners;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ClientController(ClientService clientService,
                            ModelMapper modelMapper,
                            List<ClientEventListener> listeners) {
        this.clientService = clientService;
        this.modelMapper = modelMapper;
        this.listeners = listeners;
    }

    @PostMapping
    public ResponseEntity<ClientDto> registerClient(@RequestBody ClientInfoDto clientInfoDto) throws RoomNotFoundException {
        Client client = new Client(clientInfoDto.getRoom(), clientInfoDto.getLastConnected());
        this.clientService.save(client);
        return new ResponseEntity<>(modelMapper.map(client, ClientDto.class), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Integer> unRegisterClient(@RequestParam int id) {
        int result = this.clientService.delete(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ClientDto>> getClients() {
        List<ClientDto> clientDtos = this.clientService.loadAll()
                .stream()
                .map(client -> modelMapper.map(client, ClientDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(clientDtos, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<ClientDto> updateHeartbeat(@RequestBody ClientHeartbeatDto clientHeartbeatDto) {
        List<Client> offlineClients = this.clientService.loadOfflineClients();
        Client client = this.clientService.loadById(clientHeartbeatDto.getClientId())
                .orElseThrow(ClientNotFoundException::new);
        if (offlineClients.contains(client)) {
            notifyListeners(offlineClients);
        }
        Client updatedClient = this.clientService
                .updateLastConnectedTime(clientHeartbeatDto.getClientId(), clientHeartbeatDto.getNewDate());

        return new ResponseEntity<>(modelMapper.map(updatedClient, ClientDto.class), HttpStatus.OK);
    }

    private void notifyListeners(List<Client> clients) {
        listeners.forEach(l -> l.onOfflineClients(clients));
    }
}
