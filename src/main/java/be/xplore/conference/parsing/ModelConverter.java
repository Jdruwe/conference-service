package be.xplore.conference.parsing;

import be.xplore.conference.consumer.dto.*;
import be.xplore.conference.model.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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

    public Schedule convertSchedule(ScheduleDto scheduleDto, DaysOfTheWeek daysOfTheWeek, List<Room> rooms) {
        Schedule schedule = new Schedule();
        schedule.setDay(daysOfTheWeek);
        schedule.setRooms(rooms);
        if (scheduleDto.getSlots().size() > 0) {
            LocalDate localDate = Instant.ofEpochMilli(new Date(scheduleDto.getSlots().get(0).getFromTimeMillis())
                    .getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            schedule.setDate(localDate);
        }
        return schedule;
    }

    public List<Room> convertRoomFromScheduleDto(ScheduleDto scheduleDto, List<Talk> talks) {
        List<Room> rooms = new ArrayList<>();
        for (SlotDto slotDto : scheduleDto.getSlots()) {
            Room room = new Room(
                    slotDto.getRoomId(),
                    slotDto.getRoomName(),
                    slotDto.getRoomCapacity(),
                    slotDto.getRoomSetup(),
                    talks
            );
            rooms.add(room);
        }
        return rooms;
    }

    public Talk convertTalksFromRoom(SlotDto slotDto, List<Speaker> speakers) {
        if (slotDto.getTalk() == null) {
            return null;
        }
        TalkDto talkDto = slotDto.getTalk();
        Date startTime = new Date(slotDto.getFromTimeMillis());
        Date endTime = new Date(slotDto.getToTimeMillis());
        return new Talk(
                talkDto.getId(),
                startTime,
                endTime,
                slotDto.getFromTime(),
                slotDto.getToTime(),
                talkDto.getTitle(),
                talkDto.getTalkType(),
                talkDto.getSummary(),
                speakers);
    }

    public List<Speaker> convertSpeakersForTalk(List<SpeakerDto> speakerDtoList) {
        List<Speaker> speakerList = new ArrayList<>();
        for (SpeakerDto speakerDto : speakerDtoList) {
            String[] splitHrefFromSpeaker = speakerDto.getLink().getHref().split("/");
            String uuidForSpeaker = splitHrefFromSpeaker[splitHrefFromSpeaker.length - 1];
            //TODO make extra call with uuid for data speaker
            Speaker speaker = new Speaker(uuidForSpeaker,
                    speakerDto.getName(),
                    speakerDto.getName(),
                    speakerDto.getLink().getHref(),
                    speakerDto.getLink().getHref());
            speakerList.add(speaker);
        }
        return speakerList;
    }
}
