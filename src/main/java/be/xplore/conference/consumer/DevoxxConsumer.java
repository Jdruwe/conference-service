package be.xplore.conference.consumer;

import be.xplore.conference.consumer.dto.*;
import be.xplore.conference.consumer.helper.ApiCallHelper;
import be.xplore.conference.model.*;
import be.xplore.conference.parsing.ModelConverter;
import be.xplore.conference.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
    @Value("${devoxx.api.url}")
    private String apiUrl;
    @Value("${devoxx.schedule.api.url}")
    private String scheduleUrl;
    @Value("${devoxx.rooms.api.url}")
    private String roomsUrl;
    @Value("${devoxx.speaker.api.url}")
    private String speakerUrl;

    private final ModelConverter modelConverter;
    private final ApiCallHelper apiHelper;

    private final RoomService roomService;
    private final ScheduleService scheduleService;
    private final TalkService talkService;
    private final SpeakerService speakerService;
    private final RoomScheduleService roomScheduleService;

    public DevoxxConsumer(ModelConverter modelConverter,
                          ApiCallHelper apiHelper,
                          RoomService roomService,
                          ScheduleService scheduleService,
                          TalkService talkService,
                          SpeakerService speakerService,
                          RoomScheduleService roomScheduleService) {
        this.modelConverter = modelConverter;
        this.apiHelper = apiHelper;
        this.roomService = roomService;
        this.scheduleService = scheduleService;
        this.talkService = talkService;
        this.speakerService = speakerService;
        this.roomScheduleService = roomScheduleService;
    }

    // TODO fix postConstruct or scheduler for testing
    @PostConstruct
    private void consumeApi() throws IOException {
        List<Room> rooms = processRooms(getRoomsFromApi());
        processSchedules(rooms);
    }

    private RoomsDto getRoomsFromApi() throws IOException {
        String url = apiUrl + roomsUrl;
        return apiHelper.queryApi(url, RoomsDto.class);
    }

    private ScheduleDto getRoomScheduleFromApi(String roomId, DayOfWeek day) throws IOException {
        String url = apiUrl + roomsUrl + roomId + "/" + day.name().toLowerCase();
        return apiHelper.queryApi(url, ScheduleDto.class);
    }

    private SpeakerInformationDto getSpeakerFromApi(String uuid) throws IOException {
        String url = apiUrl + speakerUrl + uuid;
        return apiHelper.queryApi(url, SpeakerInformationDto.class);
    }

    private List<Room> processRooms(RoomsDto roomsDto) {
        List<Room> rooms = modelConverter.convertToRooms(roomsDto);
        return rooms.stream().map(roomService::save).collect(Collectors.toList());
    }

    private void processSchedules(List<Room> rooms) throws IOException {
        for (Room room : rooms) {
            for (DayOfWeek day : DayOfWeek.values()) {
                ScheduleDto scheduleDto = getRoomScheduleFromApi(room.getId(), day);
                if (scheduleDto.getSlots().size() > 0) {
                    Schedule schedule = createSchedule(scheduleDto, day);
                    RoomSchedule roomSchedule = new RoomSchedule(new RoomScheduleId(schedule, room));
                    List<Talk> talks = getTalks(scheduleDto.getSlots());
                    RoomSchedule roomScheduleWithTalks = addTalksToRoomSchedule(roomSchedule, talks);
                    roomScheduleService.save(roomScheduleWithTalks);
                }
            }
        }
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
