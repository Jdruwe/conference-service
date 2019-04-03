package be.xplore.conference.converter;

import be.xplore.conference.consumer.dto.*;
import be.xplore.conference.model.*;
import be.xplore.conference.service.RoomService;
import be.xplore.conference.service.ScheduleService;
import be.xplore.conference.service.SpeakerService;
import be.xplore.conference.service.TalkService;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ModelConverter {

    private TalkService talkService;
    private SpeakerService speakerService;
    private ScheduleService scheduleService;
    private final RoomService roomService;

    public ModelConverter(TalkService talkService,
                          SpeakerService speakerService,
                          ScheduleService scheduleService, RoomService roomService) {
        this.talkService = talkService;
        this.speakerService = speakerService;
        this.scheduleService = scheduleService;
        this.roomService = roomService;
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
        if (scheduleDto.getSlots().size() > 0) {
            LocalDate localDate = Instant.ofEpochMilli(new Date(scheduleDto.getSlots().get(0).getFromTimeMillis()).getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            schedule.setDate(localDate);
        }
        scheduleService.save(schedule);
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
        rooms.forEach(roomService::save);
        return rooms;
    }

    private Talk convertTalksFromRoom(SlotDto slotDto) {
        if (slotDto.getTalk() == null) {
            return null;
        }
        TalkDto talkDto = slotDto.getTalk();
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

    private List<Speaker> convertSpeakersForTalk(List<SpeakerDto> speakerDtoList) {
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
            speakerService.save(speaker);
        }
        return speakerList;
    }
}
