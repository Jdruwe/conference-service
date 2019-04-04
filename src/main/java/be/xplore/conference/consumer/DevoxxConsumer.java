package be.xplore.conference.consumer;

import be.xplore.conference.consumer.dto.*;
import be.xplore.conference.model.*;
import be.xplore.conference.parsing.ModelConverter;
import be.xplore.conference.service.RoomService;
import be.xplore.conference.service.ScheduleService;
import be.xplore.conference.service.SpeakerService;
import be.xplore.conference.service.TalkService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DevoxxConsumer {
    @Value("${devoxx.api.url}")
    private String apiUrl;
    @Value("${devoxx.schedule.api.url}")
    private String scheduleUrl;
    @Value("${devoxx.rooms.api.url}")
    private String roomsUrl;
    @Value("${devoxx.speaker.api.url}")
    private String speakerUrl;

    private ModelConverter modelConverter;
    private final ObjectMapper objectMapper;
    private RoomService roomService;
    private ScheduleService scheduleService;
    private TalkService talkService;
    private SpeakerService speakerService;

    public DevoxxConsumer(ModelConverter modelConverter,
                          ObjectMapper objectMapper,
                          RoomService roomService,
                          ScheduleService scheduleService,
                          TalkService talkService,
                          SpeakerService speakerService) {
        this.modelConverter = modelConverter;
        this.objectMapper = objectMapper;
        this.roomService = roomService;
        this.scheduleService = scheduleService;
        this.talkService = talkService;
        this.speakerService = speakerService;
    }

    @PostConstruct
    // TODO fix postConstruct or scheduler for testing
    private void getRooms() throws IOException {
        String url = apiUrl + roomsUrl;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        RoomsDto roomsDto = objectMapper.readValue(result, RoomsDto.class);
        List<Room> rooms = modelConverter.convertRooms(roomsDto);
        rooms.forEach(r -> roomService.save(r));
        this.getSchedules(rooms);
    }

    private void getSchedules(List<Room> rooms) throws IOException {
        for (Room room : rooms) {
            for (DayOfWeek day : DayOfWeek.values()) {
                String url = apiUrl + roomsUrl + room.getId() + "/" + day.name().toLowerCase();
                RestTemplate restTemplate = new RestTemplate();
                String result = restTemplate.getForObject(url, String.class);
                ScheduleDto scheduleDto = objectMapper.readValue(result, ScheduleDto.class);
                if (scheduleDto.getSlots().size() > 0) {
                    Room roomWithTalks = addTalksToRoom(scheduleDto, room);
                    createSchedule(scheduleDto, day, roomWithTalks);
                }
            }
        }
    }

    private Room addTalksToRoom(ScheduleDto scheduleDto, Room room) throws IOException {
        List<Talk> talks = getTalks(scheduleDto.getSlots());
        Room roomWithTalks = modelConverter.addTalksToRoom(room, talks);
        return roomService.save(roomWithTalks);
    }

    private void createSchedule(ScheduleDto scheduleDto, DayOfWeek day, Room room) {
        long startTimeMillis = scheduleDto.getSlots().get(0).getFromTimeMillis();
        LocalDate date = convertMillisToDate(startTimeMillis);
        Optional<Schedule> optionalSchedule = scheduleService.loadById(date);
        Schedule schedule;
        if (optionalSchedule.isPresent()) {
            schedule = optionalSchedule.get();
            schedule.getRooms().add(room);
        } else {
            schedule = modelConverter.convertSchedule(date, day, room);
        }
        scheduleService.save(schedule);
    }

    private LocalDate convertMillisToDate(long millis) {
        return Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private List<Talk> getTalks(List<SlotDto> slotDtoList) throws IOException {
        List<Talk> talks = new ArrayList<>();
        for (SlotDto slotDto : slotDtoList) {
            Talk talk = null;
            if (slotDto.getTalk() != null) {
                List<SpeakerInformationDto> speakerInformationDtos = new ArrayList<>();
                for (SpeakerDto s : slotDto.getTalk().getSpeakers()) {
                    String link = s.getLink().getHref();
                    String[] UUIDFromHrefFromSpeaker = link.split("/");
                    String uuidForSpeaker = UUIDFromHrefFromSpeaker[UUIDFromHrefFromSpeaker.length - 1];
                    speakerInformationDtos.add(getSpeakerInformation(uuidForSpeaker));
                }
                List<Speaker> speakers = modelConverter.convertSpeakersDto(speakerInformationDtos);
                speakers.forEach(speakerService::save);
                talk = modelConverter.convertTalk(slotDto, speakers);
            }
            if (talk != null) {
                talkService.save(talk);
                talks.add(talk);
            }
        }
        return talks;
    }

    private SpeakerInformationDto getSpeakerInformation(String uuid) throws IOException {
        String url = apiUrl + speakerUrl + uuid;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        return objectMapper.readValue(result, SpeakerInformationDto.class);
    }
}
