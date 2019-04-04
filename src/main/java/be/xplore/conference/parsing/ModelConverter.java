package be.xplore.conference.parsing;

import be.xplore.conference.consumer.dto.*;
import be.xplore.conference.model.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    public Schedule convertSchedule(LocalDate date, DayOfWeek dayOfWeek, Room room) {
        return new Schedule(date, dayOfWeek, List.of(room));
    }

    public Room addTalksToRoom(Room room, List<Talk> talks) {
        if (Objects.nonNull(talks)) {
            List<Talk> roomTalks = room.getTalks();
            if (Objects.nonNull(roomTalks)) {
                room.getTalks().addAll(talks);
            } else {
                room.setTalks(talks);
            }
        }
        return room;
    }

    public Talk convertTalk(SlotDto slotDto, List<Speaker> speakers) {
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

    public List<Speaker> convertSpeakersDto(List<SpeakerInformationDto> speakerInformationDtos) {
        List<Speaker> speakerList = new ArrayList<>();
        for (SpeakerInformationDto speakerInformationDto : speakerInformationDtos) {
            Speaker speaker = new Speaker(speakerInformationDto.getUuid(),
                    speakerInformationDto.getFirstName(),
                    speakerInformationDto.getLastName(),
                    speakerInformationDto.getAvatarURL(),
                    speakerInformationDto.getTwitter());
            speakerList.add(speaker);
        }
        return speakerList;
    }
}
