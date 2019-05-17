package be.xplore.conference.rest.controller;

import be.xplore.conference.exception.RoomNotFoundException;
import be.xplore.conference.model.Client;
import be.xplore.conference.notifications.EmailSender;
import be.xplore.conference.rest.dto.ClientDto;
import be.xplore.conference.rest.dto.ClientHeartbeatDto;
import be.xplore.conference.rest.dto.ClientInfoDto;
import be.xplore.conference.schedulers.ClientScheduler;
import be.xplore.conference.service.ClientService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/client")
public class ClientController {

    private final ClientService clientService;
    private final ModelMapper modelMapper;
    private final ClientScheduler clientScheduler;
    private final EmailSender emailSender;

    public ClientController(ClientService clientService,
                            ModelMapper modelMapper,
                            ClientScheduler clientScheduler,
                            EmailSender emailSender) {
        this.clientService = clientService;
        this.modelMapper = modelMapper;
        this.clientScheduler = clientScheduler;
        this.emailSender = emailSender;
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
    public ResponseEntity<ClientDto> updateHeartbeat(@RequestBody ClientHeartbeatDto clientHeartbeatDto) throws RoomNotFoundException {
        Client client = this.clientService
                .updateLastConnectedTime(clientHeartbeatDto.getClientId(), clientHeartbeatDto.getNewDate());
        if (clientScheduler.wasClientOffline(client)) {
            emailSender.sendEmailForReconnectedClient(client);
        }
        return new ResponseEntity<>(modelMapper.map(client, ClientDto.class), HttpStatus.OK);
    }
}
