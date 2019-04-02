package be.xplore.conference.converter;

import be.xplore.conference.consumer.dto.RoomDto;
import be.xplore.conference.consumer.dto.RoomsDto;
import be.xplore.conference.model.Room;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ModelConverter {

    public List<Room> convertRooms(RoomsDto roomsDto) {
        List<Room> rooms = new ArrayList<>();
        for (RoomDto roomDto : roomsDto.getRooms()) {
            Room room = new Room(roomDto.getId(),roomDto.getName(),roomDto.getCapacity(),roomDto.getSetup());
            rooms.add(room);
        }
        return rooms;
    }
}
