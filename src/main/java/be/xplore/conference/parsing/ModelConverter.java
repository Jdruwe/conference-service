package be.xplore.conference.parsing;

import be.xplore.conference.consumer.dto.RoomsDto;
import be.xplore.conference.consumer.dto.SlotDto;
import be.xplore.conference.consumer.dto.TalkDto;
import be.xplore.conference.model.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ModelConverter {

    public List<Room> convertToRooms(RoomsDto roomsDto) {
        return roomsDto.getRooms()
                .stream()
                .map(dto -> new Room(dto.getId(), dto.getName(), dto.getCapacity(), dto.getSetup()))
                .collect(Collectors.toList());
    }

    public Schedule convertSchedule(LocalDate date, DayOfWeek dayOfWeek) {
        return new Schedule(date, dayOfWeek);
    }

    public Talk convertTalk(SlotDto slotDto, List<Speaker> speakers) {
        TalkDto talkDto = slotDto.getTalk();
        Date startTime = new Date(slotDto.getFromTimeMillis());
        Date endTime = new Date(slotDto.getToTimeMillis());
        return Talk.builder()
                .id(talkDto.getId())
                .startTime(startTime)
                .endTime(endTime)
                .fromTime(slotDto.getFromTime())
                .toTime(slotDto.getToTime())
                .title(talkDto.getTitle())
                .type(talkDto.getTalkType())
                .summary(talkDto.getSummary())
                .speakers(speakers)
                .build();
    }
}
