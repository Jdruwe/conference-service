package be.xplore.conference.converter;

import be.xplore.conference.consumer.dto.*;
import be.xplore.conference.model.*;
import be.xplore.conference.service.TalkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ModelConverter {

    private TalkService talkService;

    public ModelConverter(TalkService talkService) {
        this.talkService = talkService;
    }

    public List<Room> convertRooms(RoomsDto roomsDto) {
        List<Room> rooms = new ArrayList<>();
        for (RoomDto roomDto : roomsDto.getRooms()) {
            Room room = new Room(roomDto.getId(), roomDto.getName(), roomDto.getCapacity(), roomDto.getSetup());
            rooms.add(room);
        }
        return rooms;
    }

    public Schedule convertSchedule(ScheduleDto scheduleDto, DaysOfTheWeek daysOfTheWeek) {
        List<Room> rooms = this.convertRoomFromScheduleDto(scheduleDto);
        Schedule schedule = new Schedule();
        schedule.setDay(daysOfTheWeek);
        schedule.setRooms(rooms);
        return schedule;
    }

    private List<Room> convertRoomFromScheduleDto(ScheduleDto scheduleDto) {
        List<Room> rooms = new ArrayList<>();
        List<Talk> talks = new ArrayList<>();
        for (SlotDto slotDto : scheduleDto.getSlots()) {
            talks.add(convertTalksFromRoom(slotDto));
        }
        for (SlotDto slotDto : scheduleDto.getSlots()) {
            Room room = new Room(slotDto.getRoomId(), slotDto.getRoomName(), slotDto.getRoomCapacity(), slotDto.getRoomSetup(), talks);
            rooms.add(room);
        }
        return rooms;
    }

    private Talk convertTalksFromRoom(SlotDto slotDto) {
        if (slotDto.getTalk() == null) {
            return null;
        }
        TalkDto talkDto = slotDto.getTalk();
        log.info("==================================");
        log.warn(talkDto.getId());
        log.warn(slotDto.getRoomId());
        log.info("==================================");
        List<Speaker> speakers = convertSpeakersForTalk(slotDto.getTalk().getSpeakers());
        Date startTime = new Date(slotDto.getFromTimeMillis());
        Date endTime = new Date(slotDto.getToTimeMillis());
        Talk talk = new Talk(
                talkDto.getId(),
                startTime,
                endTime,
                slotDto.getFromTime(),
                slotDto.getToTime(),
                talkDto.getTitle(),
                talkDto.getTalkType(),
                talkDto.getSummary(),
                speakers);
        talkService.save(talk);
        return talk;
    }

    private static final Logger log = LoggerFactory.getLogger(ModelConverter.class);

    private List<Speaker> convertSpeakersForTalk(List<SpeakerDto> speakerDtoList) {
        List<Speaker> speakerList = new ArrayList<>();
        for (SpeakerDto speakerDto : speakerDtoList) {
            String[] splitHrefFromSpeaker = speakerDto.getLink().getHref().split("/");
            String uuidForSpeaker = splitHrefFromSpeaker[splitHrefFromSpeaker.length - 1];
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
