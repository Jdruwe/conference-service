package be.xplore.conference.consumer.processor;

import be.xplore.conference.consumer.api.ApiCaller;
import be.xplore.conference.consumer.api.dto.RoomScheduleResponse;
import be.xplore.conference.consumer.api.dto.SpeakerResponse;
import be.xplore.conference.consumer.dto.ScheduleDto;
import be.xplore.conference.model.*;
import be.xplore.conference.parsing.converter.model.ScheduleConverter;
import be.xplore.conference.parsing.converter.util.MillisConverter;
import be.xplore.conference.service.RoomScheduleService;
import be.xplore.conference.service.ScheduleService;
import be.xplore.conference.service.SpeakerService;
import be.xplore.conference.service.TalkService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class ScheduleProcessor {

    private final RoomScheduleService roomScheduleService;
    private final ScheduleService scheduleService;
    private final TalkService talkService;
    private final SpeakerService speakerService;

    private final TalkProcessor talkProcessor;

    private final ApiCaller apiCaller;

    public ScheduleProcessor(RoomScheduleService roomScheduleService,
                             ScheduleService scheduleService,
                             TalkService talkService,
                             SpeakerService speakerService,
                             TalkProcessor talkProcessor,
                             ApiCaller apiCaller) {
        this.roomScheduleService = roomScheduleService;
        this.scheduleService = scheduleService;
        this.talkService = talkService;
        this.speakerService = speakerService;
        this.talkProcessor = talkProcessor;
        this.apiCaller = apiCaller;
    }

    public void process(List<Room> rooms) {
        for (Room room : rooms) {
            createScheduleForRoomForWeek(room);
        }
    }

    private void createScheduleForRoomForWeek(Room room) {
        Arrays.stream(DayOfWeek.values())
                .forEach(d -> createScheduleForRoomForDay(room, d));
    }

    private void createScheduleForRoomForDay(Room room, DayOfWeek day) {
        RoomSchedule rs = getRoomSchedule(day, room.getId());

        RoomScheduleResponse response = apiCaller.getRoomSchedule(room.getId(),
                getRoomScheduleEtag(rs),
                day);
        ScheduleDto scheduleDto = response.getSchedule();

        if (hasSchedule(scheduleDto)) {
            generateRoomSchedule(scheduleDto, day, room, response.getEtag());
        } else if (hasTalks(rs)) {
            updateTalks(rs);
        }
    }

    private RoomSchedule getRoomSchedule(DayOfWeek day, String roomId) {
        Optional<RoomSchedule> optionalRs = roomScheduleService.loadByDayAndRoomId(day, roomId);
        return optionalRs.orElse(null);
    }

    private String getRoomScheduleEtag(RoomSchedule roomSchedule) {
        return Objects.nonNull(roomSchedule) ? roomSchedule.getEtag() : "";
    }

    private void updateTalks(RoomSchedule roomSchedule) {
        roomSchedule.getTalks().forEach(talk -> {
            Optional<Talk> t = talkService.loadById(talk.getId());
            if (t.isPresent() && hasSpeakers(t.get())) {
                updateSpeaker(t.get());
            }
        });
    }

    private void updateSpeaker(Talk talk) {
        for (Speaker speaker : talk.getSpeakers()) {
            SpeakerResponse speakerResponse = apiCaller.getSpeaker(speaker.getUuid(), speaker.getEtag());
            if (Objects.nonNull(speakerResponse.getSpeakerInformation())) {
                speaker = speakerResponse.getSpeakerInformation().toDomain();
                speakerService.save(speaker);
            }
        }
    }

    private void generateRoomSchedule(ScheduleDto dto, DayOfWeek day, Room room, String etag) {
        Schedule schedule = createSchedule(dto, day);
        List<Talk> talks = talkProcessor.process(dto.getSlots());
        createRoomSchedule(schedule, room, etag, talks);
    }

    private boolean hasSchedule(ScheduleDto dto) {
        return Objects.nonNull(dto) && dto.getSlots().size() > 0;
    }

    private boolean hasTalks(RoomSchedule roomSchedule) {
        return Objects.nonNull(roomSchedule) && Objects.nonNull(roomSchedule.getTalks());
    }

    private boolean hasSpeakers(Talk talk) {
        return talk.getSpeakers().size() > 0;
    }

    private void createRoomSchedule(Schedule schedule, Room room, String etag, List<Talk> talks) {
        RoomSchedule roomSchedule = new RoomSchedule(new RoomScheduleId(schedule, room));
        addTalksToRoomSchedule(roomSchedule, talks);
        roomSchedule.setEtag(etag);
        roomScheduleService.save(roomSchedule);
    }

    private Schedule createSchedule(ScheduleDto scheduleDto, DayOfWeek day) {
        LocalDate date = getStartDate(scheduleDto);
        Optional<Schedule> optionalSchedule = scheduleService.loadById(date);
        if (optionalSchedule.isPresent()) {
            return optionalSchedule.get();
        } else {
            Schedule schedule = ScheduleConverter.toSchedule(date, day);
            return scheduleService.save(schedule);
        }
    }

    private void addTalksToRoomSchedule(RoomSchedule roomSchedule, List<Talk> talks) {
        if (Objects.nonNull(talks)) {
            List<Talk> roomTalks = roomSchedule.getTalks();
            if (Objects.nonNull(roomTalks)) {
                roomSchedule.getTalks().addAll(talks);
            } else {
                roomSchedule.setTalks(talks);
            }
        }
    }

    private LocalDate getStartDate(ScheduleDto scheduleDto) {
        long startTimeMillis = scheduleDto.getSlots().get(0).getFromTimeMillis();
        return MillisConverter.toDate(startTimeMillis);
    }
}
