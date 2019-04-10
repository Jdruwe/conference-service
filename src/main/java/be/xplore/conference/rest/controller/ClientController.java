package be.xplore.conference.rest.controller;

import be.xplore.conference.excpetion.RoomAlreadyRegisteredException;
import be.xplore.conference.excpetion.RoomNotFoundException;
import be.xplore.conference.model.Client;
import be.xplore.conference.rest.dto.ClientDto;
import be.xplore.conference.service.ClientService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity unRegisterClient(@RequestParam String id ) throws RoomNotFoundException {
        this.clientService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
