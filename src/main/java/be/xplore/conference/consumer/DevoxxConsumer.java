package be.xplore.conference.consumer;

import be.xplore.conference.consumer.dto.*;
import be.xplore.conference.consumer.helper.ApiCallHelper;
import be.xplore.conference.consumer.helper.dto.ApiResponse;
import be.xplore.conference.consumer.helper.dto.RoomScheduleResponse;
import be.xplore.conference.consumer.helper.dto.SpeakerResponse;
import be.xplore.conference.consumer.property.DevoxxApiProperties;
import be.xplore.conference.model.*;
import be.xplore.conference.parsing.ModelConverter;
import be.xplore.conference.property.SettingsProperties;
import be.xplore.conference.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(DevoxxConsumer.class);

    private final DevoxxApiProperties apiProperties;
    private final SettingsProperties settingsProperties;

    private final ModelConverter modelConverter;
    private final ApiCallHelper apiHelper;

    private final RoomService roomService;
    private final ScheduleService scheduleService;
    private final TalkService talkService;
    private final SpeakerService speakerService;
    private final RoomScheduleService roomScheduleService;
    private final SettingsService settingsService;

    public DevoxxConsumer(ModelConverter modelConverter,
                          ApiCallHelper apiHelper,
                          RoomService roomService,
                          ScheduleService scheduleService,
                          TalkService talkService,
                          SpeakerService speakerService,
                          RoomScheduleService roomScheduleService,
                          SettingsService settingsService,
                          DevoxxApiProperties apiProperties,
                          SettingsProperties settingsProperties) {
        this.modelConverter = modelConverter;
        this.apiHelper = apiHelper;
        this.roomService = roomService;
        this.scheduleService = scheduleService;
        this.talkService = talkService;
        this.speakerService = speakerService;
        this.roomScheduleService = roomScheduleService;
        this.settingsService = settingsService;
        this.apiProperties = apiProperties;
        this.settingsProperties = settingsProperties;
    }

    @Scheduled(fixedRateString = "${settings.queryRateInMilliseconds}")
    private void consumeApi() throws IOException {
        String etag = getRoomsEtag();

        RoomsDto dto = getRoomsFromApi(etag);
        List<Room> rooms;
        if (Objects.nonNull(dto)) {
            rooms = processRooms(dto);
        } else {
            rooms = roomService.loadAll();
        }
        processSchedules(rooms);
        fillSettings();

        LOGGER.info("Done");
    }

    private void fillSettings() {
        settingsService.save(new Settings("minutesBeforeNextSession", String.valueOf(settingsProperties.getMinutesBeforeNextSession())));
        settingsService.save(new Settings("isRoomOccupancyOn", String.valueOf(settingsProperties.getQueryRateInMilliseconds())));
    }

    private RoomsDto getRoomsFromApi(String etag) throws IOException {
        String url = apiProperties.getBaseUrl() + apiProperties.getRooms();
        ApiResponse response = apiHelper.queryApi(url, etag, RoomsDto.class);
        saveRoomsEtag(response.getETag());
        return (RoomsDto) response.getBody();
    }

    private RoomScheduleResponse getRoomScheduleFromApi(String roomId, String etag, DayOfWeek day) throws IOException {
        String url = apiProperties.getBaseUrl() + apiProperties.getRooms() + roomId + "/" + day.name().toLowerCase();
        ApiResponse response = apiHelper.queryApi(url, etag, ScheduleDto.class);

        return new RoomScheduleResponse(response.getETag(), (ScheduleDto) response.getBody());
    }

    private SpeakerResponse getSpeakerFromApi(String uuid, String etag) throws IOException {
        String url = apiProperties.getBaseUrl() + apiProperties.getSpeaker() + uuid;
        ApiResponse response = apiHelper.queryApi(url, etag, SpeakerInformationDto.class);
        return new SpeakerResponse(response.getETag(), (SpeakerInformationDto) response.getBody());
    }

    private String getRoomsEtag() {
        Optional<Settings> etagSetting = settingsService.loadByKey("roomsEtag");

        if (etagSetting.isPresent()) {
            return etagSetting.get().getValue();
        }
        return "";
    }

    private void saveRoomsEtag(String etag) {
        settingsService.save(new Settings("roomsEtag", etag));
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
        List<RoomSchedule> schedules = roomScheduleService.loadByDayAndRoomId(day, room.getId());
        String etag = "";
        if (schedules.size() > 0) {
            etag = schedules.get(0).getEtag();
        }

        RoomScheduleResponse response = getRoomScheduleFromApi(room.getId(), etag, day);
        ScheduleDto scheduleDto = response.getSchedule();

        if (Objects.nonNull(scheduleDto) && scheduleDto.getSlots().size() > 0) {
            Schedule schedule = createSchedule(scheduleDto, day);
            List<Talk> talks = processTalks(scheduleDto.getSlots());
            createRoomSchedule(schedule, room, talks, response.getEtag());
        }
    }

    private void createRoomSchedule(Schedule schedule, Room room, List<Talk> talks, String etag) {
        RoomSchedule roomSchedule = new RoomSchedule(new RoomScheduleId(schedule, room));
        RoomSchedule roomScheduleWithTalks = addTalksToRoomSchedule(roomSchedule, talks);
        roomScheduleWithTalks.setEtag(etag);
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

    private List<Talk> processTalks(List<SlotDto> slotDtoList) throws IOException {
        List<Talk> talks = new ArrayList<>();
        if (Objects.nonNull(slotDtoList)) {
            for (SlotDto slotDto : slotDtoList) {
                Optional<Talk> t = getTalkFromSlot(slotDto);
                if (t.isPresent()) {
                    talkService.save(t.get());
                    talks.add(t.get());
                }
            }
        }
        return talks;
    }

    private Optional<Talk> getTalkFromSlot(SlotDto slot) throws IOException {
        if (Objects.nonNull(slot.getTalk())) {
            List<Speaker> speakers = getSpeakersForTalk(slot.getTalk().getSpeakers());
            return Optional.of(modelConverter.convertTalk(slot, speakers));
        }
        return Optional.empty();
    }

    private List<Speaker> getSpeakersForTalk(List<SpeakerDto> speakerList) throws IOException {
        List<Speaker> speakers = new ArrayList<>();
        for (SpeakerDto dto : speakerList) {
            speakers.add(createSpeaker(dto));
        }
        return speakers;
    }

    private Speaker createSpeaker(SpeakerDto dto) throws IOException {
        String href = dto.getLink().getHref();
        String uuid = href.substring(href.lastIndexOf('/'));
        String etag = getSpeakerEtag(uuid);

        SpeakerResponse response = getSpeakerFromApi(uuid, etag);
        Speaker s = response.getSpeakerInformation().toDomain();
        s.setEtag(response.getEtag());
        return speakerService.save(s);
    }

    private String getSpeakerEtag(String uuid) {
        Optional<Speaker> optionalSpeaker = speakerService.loadById(uuid);
        if (optionalSpeaker.isPresent()) {
            return optionalSpeaker.get().getEtag();
        }
        return "";
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
