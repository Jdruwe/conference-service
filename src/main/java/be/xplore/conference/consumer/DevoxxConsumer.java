package be.xplore.conference.consumer;

import be.xplore.conference.consumer.dto.*;
import be.xplore.conference.consumer.helper.ApiCallHelper;
import be.xplore.conference.consumer.helper.dto.ApiResponse;
import be.xplore.conference.model.*;
import be.xplore.conference.parsing.ModelConverter;
import be.xplore.conference.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DevoxxConsumer {

    private String roomsEtag;

    @Value("${devoxx.api.url}")
    private String apiUrl;
    @Value("${devoxx.schedule.api.url}")
    private String scheduleUrl;
    @Value("${devoxx.rooms.api.url}")
    private String roomsUrl;
    @Value("${devoxx.speaker.api.url}")
    private String speakerUrl;

    @Value("${devoxx.settings.isRoomOccupancyOn}")
    private String isRoomOccupancyOn;
    @Value("${devoxx.settings.minutesBeforeNextSession}")
    private String minutesBeforeNextSession;

    private final ModelConverter modelConverter;
    private final ApiCallHelper apiHelper;

    private final RoomService roomService;
    private final ScheduleService scheduleService;
    private final TalkService talkService;
    private final SpeakerService speakerService;
    private final RoomScheduleService roomScheduleService;
    private final SettingsService settingsService;
    private final ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(DevoxxConsumer.class);

    public DevoxxConsumer(ModelConverter modelConverter,
                          ApiCallHelper apiHelper,
                          RoomService roomService,
                          ScheduleService scheduleService,
                          TalkService talkService,
                          SpeakerService speakerService,
                          RoomScheduleService roomScheduleService,
                          SettingsService settingsService, ObjectMapper objectMapper) {
        this.modelConverter = modelConverter;
        this.apiHelper = apiHelper;
        this.roomService = roomService;
        this.scheduleService = scheduleService;
        this.talkService = talkService;
        this.speakerService = speakerService;
        this.roomScheduleService = roomScheduleService;
        this.settingsService = settingsService;
        this.objectMapper = objectMapper;
        settingsService.loadByKey("roomsEtag").ifPresent(settings -> roomsEtag = settings.getValue());
    }

    @Scheduled(fixedRateString = "${query.api.rate.milliseconds}")
    private void consumeApi() throws IOException {
//        RoomsDto dto = getRoomsFromApi("2030473898");
        RoomsDto dto = getRoomsFromApi(roomsEtag);
        List<Room> rooms;
        if (Objects.nonNull(dto)) {
            rooms = processRooms(dto);
        } else {
            rooms = roomService.loadAll();
        }
        processSchedules(rooms);
        fillSettings();
    }

    private void fillSettings() {
        settingsService.save(new Settings("minutesBeforeNextSession", minutesBeforeNextSession));
        settingsService.save(new Settings("isRoomOccupancyOn", isRoomOccupancyOn));
    }

    private RoomsDto getRoomsFromApi(String etag) throws IOException {
        String url = apiUrl + roomsUrl;
        ApiResponse response = apiHelper.queryApi(url, etag, RoomsDto.class);
        // todo save etag
        return (RoomsDto) response.getBody();
    }

    // todo fix me
    private ScheduleDto getRoomScheduleFromApi(String roomId, DayOfWeek day) throws IOException {
        Optional<Room> room = roomService.loadById(roomId);
        String etag = "";
        if (room.isPresent()) {
            etag = room.get().getEtag();
        }
        String url = apiUrl + roomsUrl + roomId + "/" + day.name().toLowerCase();
        ApiResponse response = apiHelper.queryApi(url, etag, ScheduleDto.class);
        // todo save etag
        return (ScheduleDto) response.getBody();
    }

    // todo fix me
    private SpeakerInformationDto getSpeakerFromApi(String uuid) throws IOException {
        String url = apiUrl + speakerUrl + uuid;
//        return apiHelper.queryApi(url, SpeakerInformationDto.class);
        return null;
    }

    private List<Room> processRooms(RoomsDto roomsDto) {
        List<Room> rooms = modelConverter.convertToRooms(roomsDto);
        return rooms.stream().map(roomService::save).collect(Collectors.toList());
    }

    private void processSchedules(List<Room> rooms) throws IOException {
        for (Room room : rooms) {
            createScheduleForRoomForWeek(room);
        }
    }

    private void createScheduleForRoomForWeek(Room room) throws IOException {
        for (DayOfWeek day : DayOfWeek.values()) {
            createScheduleForRoomForDay(room, day);
        }
    }

    private void createScheduleForRoomForDay(Room room, DayOfWeek day) throws IOException {
        ScheduleDto scheduleDto = getRoomScheduleFromApi(room.getId(), day);

        if (scheduleDto.getSlots().size() > 0) {
            Schedule schedule = createSchedule(scheduleDto, day);
            List<Talk> talks = getTalks(scheduleDto.getSlots());
            createRoomSchedule(schedule, room, talks);
        }
    }

    private void createRoomSchedule(Schedule schedule, Room room, List<Talk> talks) {
        RoomSchedule roomSchedule = new RoomSchedule(new RoomScheduleId(schedule, room));
        RoomSchedule roomScheduleWithTalks = addTalksToRoomSchedule(roomSchedule, talks);
        roomScheduleService.save(roomScheduleWithTalks);
    }

    private Schedule createSchedule(ScheduleDto scheduleDto, DayOfWeek day) {
        long startTimeMillis = scheduleDto.getSlots().get(0).getFromTimeMillis();
        LocalDate date = convertMillisToDate(startTimeMillis);
        Optional<Schedule> optionalSchedule = scheduleService.loadById(date);
        if (optionalSchedule.isPresent()) {
            return optionalSchedule.get();
        } else {
            Schedule schedule = modelConverter.convertSchedule(date, day);
            return scheduleService.save(schedule);
        }
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
                    speakerInformationDtos.add(getSpeakerFromApi(uuidForSpeaker));
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

    private RoomSchedule addTalksToRoomSchedule(RoomSchedule roomSchedule, List<Talk> talks) {
        if (Objects.nonNull(talks)) {
            List<Talk> roomTalks = roomSchedule.getTalks();
            if (Objects.nonNull(roomTalks)) {
                roomSchedule.getTalks().addAll(talks);
            } else {
                roomSchedule.setTalks(talks);
            }
        }
        return roomSchedule;
    }

    private LocalDate convertMillisToDate(long millis) {
        return Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
