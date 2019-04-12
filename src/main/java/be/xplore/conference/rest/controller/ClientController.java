package be.xplore.conference.rest.controller;

import be.xplore.conference.excpetion.RoomAlreadyRegisteredException;
import be.xplore.conference.excpetion.RoomNotFoundException;
import be.xplore.conference.model.Client;
import be.xplore.conference.rest.dto.ClientDto;
import be.xplore.conference.rest.dto.ClientHeartbeatDto;
import be.xplore.conference.service.ClientService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/client")
public class ClientController {

    private ClientService clientService;
    private ModelMapper modelMapper;

    public ClientController(ClientService clientService, ModelMapper modelMapper) {
        this.clientService = clientService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<ClientDto> registerClient(@RequestBody ClientDto clientDto) throws RoomAlreadyRegisteredException, RoomNotFoundException {
        Client client = new Client(clientDto.getRoom(), clientDto.getLastConnected());
        this.clientService.save(client);
        return new ResponseEntity<>(modelMapper.map(client, ClientDto.class), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Integer> unRegisterClient(@RequestParam String id) throws RoomNotFoundException {
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

    private static final Logger log = LoggerFactory.getLogger(ClientController.class);

    @PatchMapping
    public ResponseEntity<ClientDto> updateHeartbeat(@RequestBody ClientHeartbeatDto clientHeartbeatDto) throws RoomNotFoundException {
        log.info("Heartbeat update for room "
                + clientHeartbeatDto.getRoomId() +
                " at time of " +
                clientHeartbeatDto.getNewDate());
        Client client = this.clientService.updateLastConnectedTime(clientHeartbeatDto.getRoomId(), clientHeartbeatDto.getNewDate());
        return new ResponseEntity<>(modelMapper.map(client, ClientDto.class),HttpStatus.OK);
    }
}
