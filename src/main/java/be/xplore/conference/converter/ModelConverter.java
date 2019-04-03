package be.xplore.conference.converter;

import be.xplore.conference.consumer.dto.RoomDto;
import be.xplore.conference.consumer.dto.RoomsDto;
import be.xplore.conference.consumer.dto.ScheduleDto;
import be.xplore.conference.consumer.dto.SlotDto;
import be.xplore.conference.model.DaysOfTheWeek;
import be.xplore.conference.model.Room;
import be.xplore.conference.model.Schedule;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ModelConverter {

    public List<Room> convertRooms(RoomsDto roomsDto) {
        List<Room> rooms = new ArrayList<>();
        for (RoomDto roomDto : roomsDto.getRooms()) {
            Room room = new Room(roomDto.getId(), roomDto.getName(), roomDto.getCapacity(), roomDto.getSetup());
            rooms.add(room);
        }
        return rooms;
    }

    public Schedule convertSchedule(ScheduleDto scheduleDto, DaysOfTheWeek daysOfTheWeek) {
        Schedule schedule = new Schedule();
        schedule.setDay(daysOfTheWeek);
        return schedule;
    }

    private List<Room> convertRoomFromScheduleDto(ScheduleDto scheduleDto) {
        List<Room> rooms = new ArrayList<>();
        for (SlotDto slotDto : scheduleDto.getSlots()) {
            Room room = new Room(
                    slotDto.getRoomId(),
                    slotDto.getRoomName(),
                    slotDto.getRoomCapacity(),
                    slotDto.getRoomSetup()
            );
            rooms.add(room);
        }
        return rooms;
    }
}
