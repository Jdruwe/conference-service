package be.xplore.conference.parsing.converter.model;

import be.xplore.conference.consumer.dto.RoomsDto;
import be.xplore.conference.model.Room;

import java.util.List;
import java.util.stream.Collectors;

public class RoomConverter {
    public static List<Room> toRooms(RoomsDto roomsDto) {
        return roomsDto.getRooms()
                .stream()
                .map(dto -> Room.builder()
                        .id(dto.getId())
                        .name(dto.getName())
                        .capacity(dto.getCapacity())
                        .setup(dto.getSetup())
                        .build()
                ).collect(Collectors.toList());
    }
}
