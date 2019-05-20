package be.xplore.conference.processor;

import be.xplore.conference.converter.model.RoomConverter;
import be.xplore.conference.dto.RoomsDto;
import be.xplore.conference.model.Room;
import be.xplore.conference.service.RoomService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomProcessor {

    private final RoomService roomService;

    public RoomProcessor(RoomService roomService) {
        this.roomService = roomService;
    }

    public List<Room> process(RoomsDto roomsDto) {
        List<Room> rooms = RoomConverter.toRooms(roomsDto);
        return rooms.stream()
                .map(roomService::save)
                .collect(Collectors.toList());
    }
}
