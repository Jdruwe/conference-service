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
import java.util.ArrayList;
import java.util.List;

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
        for (Room room : rooms) {
            roomService.save(room);
        }
        this.getSchedules();
    }

    private void getSchedules() throws IOException {
        List<Room> rooms = roomService.loadAll();
        for (DaysOfTheWeek day : DaysOfTheWeek.values()) {
            for (Room room : rooms) {
                String url = apiUrl + roomsUrl + room.getId() + "/" + day.name().toLowerCase();
                RestTemplate restTemplate = new RestTemplate();
                String result = restTemplate.getForObject(url, String.class);
                ScheduleDto scheduleDto = objectMapper.readValue(result, ScheduleDto.class);
                List<Talk> talks = new ArrayList<>();
                for (SlotDto slotDto : scheduleDto.getSlots()) {
                    Talk talk = null;
                    if (slotDto.getTalk() != null) {
                        List<SpeakerInformationDto> speakerInformationDtos = new ArrayList<>();
                        for (SpeakerDto s : slotDto.getTalk().getSpeakers()) {
                            String link = s.getLink().getHref();
                            String[] splitHrefFromSpeaker = link.split("/");
                            String uuidForSpeaker = splitHrefFromSpeaker[splitHrefFromSpeaker.length - 1];
                            speakerInformationDtos.add(getSpeakerInformation(uuidForSpeaker));
                        }
                        List<Speaker> speakers = modelConverter.convertSpeakersForTalk(speakerInformationDtos);
                        speakers.forEach(speakerService::save);
                        talk = modelConverter.convertTalksFromRoom(slotDto, speakers);
                    }
                    if (talk != null) {
                        talkService.save(talk);
                        talks.add(talk);
                    }
                }
                List<Room> convertedRooms = modelConverter.convertRoomFromScheduleDto(scheduleDto, talks);
                convertedRooms.forEach(roomService::save);
                Schedule schedule = modelConverter.convertSchedule(scheduleDto, day, rooms);
                scheduleService.save(schedule);
            }
        }
    }

    private SpeakerInformationDto getSpeakerInformation(String uuid) throws IOException {
        String url = apiUrl + speakerUrl + uuid;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        return objectMapper.readValue(result, SpeakerInformationDto.class);
    }
}
