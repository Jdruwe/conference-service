package be.xplore.conference.consumer;

import be.xplore.conference.consumer.dto.RoomsDto;
import be.xplore.conference.consumer.dto.ScheduleDto;
import be.xplore.conference.converter.ModelConverter;
import be.xplore.conference.model.DaysOfTheWeek;
import be.xplore.conference.model.Room;
import be.xplore.conference.model.Schedule;
import be.xplore.conference.service.RoomService;
import be.xplore.conference.service.ScheduleService;
import be.xplore.conference.service.SpeakerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.Console;
import java.io.IOException;
import java.util.List;

@Component
public class DevoxxConsumer {
    @Value("${devoxx.api.url}")
    private String apiUrl;
    @Value("${devoxx.schedule.api.url}")
    private String scheduleUrl;
    @Value("${devoxx.rooms.api.url}")
    private String roomsUrl;
    @Value("${devoxx.scheduleForRoomForDay.api.url}")
    private String scheduleForDayForRoom;

    private ModelConverter modelConverter;
    private final ObjectMapper objectMapper;
    private RoomService roomService;
    private ScheduleService scheduleService;
    private SpeakerService speakerService;
    private static final Logger log = LoggerFactory.getLogger(DevoxxConsumer.class);

    public DevoxxConsumer(ModelConverter modelConverter,
                          ObjectMapper objectMapper,
                          RoomService roomService,
                          ScheduleService scheduleService,
                          SpeakerService speakerService) {
        this.modelConverter = modelConverter;
        this.objectMapper = objectMapper;
        this.roomService = roomService;
        this.scheduleService = scheduleService;
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
        this.getScheduleForRoomForDay();
    }

    private void getScheduleForRoomForDay() throws IOException {
        List<Room> rooms = roomService.loadAll();
        for (DaysOfTheWeek day : DaysOfTheWeek.values()) {
            for (Room room : rooms) {
                String url = apiUrl + scheduleForDayForRoom + room.getId() + "/" + day.name().toLowerCase();
                RestTemplate restTemplate = new RestTemplate();
                String result = restTemplate.getForObject(url, String.class);
                ScheduleDto scheduleDto = objectMapper.readValue(result, ScheduleDto.class);
                log.info("::::::::::::::::::");
                log.warn(day.name());
                log.warn(room.getName());
                log.warn(String.valueOf(scheduleDto.getSlots().size()));
                log.info("::::::::::::::::::");
                Schedule schedule = modelConverter.convertSchedule(scheduleDto, day);
                scheduleService.save(schedule);
                // TODO complete or remove this method
            }
        }
    }
}
