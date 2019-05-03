package be.xplore.conference.rest.controller;

import be.xplore.conference.rest.dto.RoomDto;
import be.xplore.conference.service.RoomService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/room")
public class RoomController {
    private final ModelMapper modelMapper;
    private final RoomService roomService;

    public RoomController(ModelMapper modelMapper, RoomService roomService) {
        this.modelMapper = modelMapper;
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<List<RoomDto>> getRooms() {
        List<RoomDto> roomDtos = roomService.loadAll()
                .stream()
                .map(room -> modelMapper.map(room, RoomDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(roomDtos, HttpStatus.OK);
    }
}
