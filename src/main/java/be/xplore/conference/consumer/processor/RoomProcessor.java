package be.xplore.conference.consumer.processor;

import be.xplore.conference.consumer.dto.RoomsDto;
import be.xplore.conference.model.Room;
import be.xplore.conference.parsing.ModelConverter;
import be.xplore.conference.service.RoomService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomProcessor {

    private final RoomService roomService;
    private final ModelConverter modelConverter;

    public RoomProcessor(RoomService roomService,
                         ModelConverter modelConverter) {
        this.roomService = roomService;
        this.modelConverter = modelConverter;
    }

    public List<Room> process(RoomsDto roomsDto) {
        List<Room> rooms = modelConverter.convertToRooms(roomsDto);
        return rooms.stream()
                .map(roomService::save)
                .collect(Collectors.toList());
    }
}
