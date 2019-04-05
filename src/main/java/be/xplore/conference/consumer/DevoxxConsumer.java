package be.xplore.conference.consumer;

import be.xplore.conference.consumer.dto.*;
import be.xplore.conference.model.*;
import be.xplore.conference.parsing.ModelConverter;
import be.xplore.conference.service.*;
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
    private final RoomScheduleService roomScheduleService;

    public DevoxxConsumer(ModelConverter modelConverter,
                          ObjectMapper objectMapper,
                          RoomService roomService,
                          ScheduleService scheduleService,
                          TalkService talkService,
                          SpeakerService speakerService, RoomScheduleService roomScheduleService) {
        this.modelConverter = modelConverter;
        this.objectMapper = objectMapper;
        this.roomService = roomService;
        this.scheduleService = scheduleService;
        this.talkService = talkService;
        this.speakerService = speakerService;
        this.roomScheduleService = roomScheduleService;
    }

    // TODO fix postConstruct or scheduler for testing
    @PostConstruct
    private void consumeApi() throws IOException {
//        Room room8 = new Room("Room8", "Room 8", 0, "");
//        Room room5 = new Room("Room5", "Room 5", 0, "");
//        roomService.save(room8);
//        roomService.save(room5);
//
//        Schedule schedule1 = new Schedule(LocalDate.of(2018, 11, 12), DayOfWeek.MONDAY);
//        Schedule schedule2 = new Schedule(LocalDate.of(2018, 11, 13), DayOfWeek.TUESDAY);
//        Schedule schedule3 = new Schedule(LocalDate.of(2018, 11, 14), DayOfWeek.WEDNESDAY);
//        scheduleService.save(schedule1);
//        scheduleService.save(schedule2);
//        scheduleService.save(schedule3);
//
//        Talk talk1 = new Talk("Talk 1", new Date(), new Date(), "10", "11", "Talk 1", "Type 1", "Summary", null);
//        Talk talk2 = new Talk("Talk 2", new Date(), new Date(), "10", "11", "Talk 1", "Type 1", "Summary", null);
//        Talk talk3 = new Talk("Talk 3", new Date(), new Date(), "10", "11", "Talk 1", "Type 1", "Summary", null);
//        talkService.save(talk1);
//        talkService.save(talk2);
//        talkService.save(talk3);
//
//        RoomSchedule roomSchedule1 = new RoomSchedule(new RoomScheduleId(schedule1, room8), List.of(talk1));
//        RoomSchedule roomSchedule2 = new RoomSchedule(new RoomScheduleId(schedule2, room8), List.of(talk2));
//        RoomSchedule roomSchedule3 = new RoomSchedule(new RoomScheduleId(schedule1, room5), List.of(talk3));
//        roomScheduleService.save(roomSchedule1);
//        roomScheduleService.save(roomSchedule2);
//        roomScheduleService.save(roomSchedule3);

        List<Room> rooms = getRoomsFromApi();
        getSchedules(rooms);
    }

    private List<Room> getRoomsFromApi() throws IOException {
        String url = apiUrl + roomsUrl;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        RoomsDto roomsDto = objectMapper.readValue(result, RoomsDto.class);
        List<Room> rooms = modelConverter.convertRooms(roomsDto);
        rooms.forEach(r -> roomService.save(r));
        return rooms;
    }

    private void getSchedules(List<Room> rooms) throws IOException {
        for (Room room : rooms) {
            for (DayOfWeek day : DayOfWeek.values()) {
                ScheduleDto scheduleDto = getRoomScheduleFromApi(room.getId(), day);
                if (scheduleDto.getSlots().size() > 0) {
                    Schedule schedule = createSchedule(scheduleDto, day);
                    //RoomSchedule roomSchedule
//                    addTalksToSchedule(scheduleDto, schedule);
                }
            }
        }
    }

    private ScheduleDto getRoomScheduleFromApi(String roomId, DayOfWeek day) throws IOException {
        String url = apiUrl + roomsUrl + roomId + "/" + day.name().toLowerCase();
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        return objectMapper.readValue(result, ScheduleDto.class);
    }

    private Schedule addTalksToSchedule(ScheduleDto scheduleDto, Schedule schedule) throws IOException {
        List<Talk> talks = getTalks(scheduleDto.getSlots());
        Schedule scheduleWithTalks = modelConverter.addTalksToSchedule(schedule, talks);
        return scheduleService.save(scheduleWithTalks);
    }

    private Schedule createSchedule(ScheduleDto scheduleDto, DayOfWeek day) {
        long startTimeMillis = scheduleDto.getSlots().get(0).getFromTimeMillis();
        LocalDate date = convertMillisToDate(startTimeMillis);
        Optional<Schedule> optionalSchedule = scheduleService.loadById(date);
        Schedule schedule;
        schedule = optionalSchedule.orElse(modelConverter.convertSchedule(date, day));
        return scheduleService.save(schedule);
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
